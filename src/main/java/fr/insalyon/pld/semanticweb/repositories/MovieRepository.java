package fr.insalyon.pld.semanticweb.repositories;

import fr.insalyon.pld.semanticweb.entities.Movie;
import fr.insalyon.pld.semanticweb.model.persistence.SchemaLinker;
import fr.insalyon.pld.semanticweb.services.sparqldsl.QueryBuilderWhere;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static fr.insalyon.pld.semanticweb.model.persistence.SchemaLinker.*;
import static fr.insalyon.pld.semanticweb.model.persistence.SchemaLinker.IS;
import static fr.insalyon.pld.semanticweb.model.tuple.Triplet.tripletOf;
import static fr.insalyon.pld.semanticweb.services.sparqldsl.Filter.hasUri;
import static fr.insalyon.pld.semanticweb.services.sparqldsl.Filter.lang;
import static fr.insalyon.pld.semanticweb.services.sparqldsl.Filter.like;
import static fr.insalyon.pld.semanticweb.services.sparqldsl.QueryBuilderImpl.select;

@Component("MovieRepository")
public class MovieRepository implements SPARQLRepository<Movie> {

    @Override
    public Optional<Movie> findById(String uri) {

        String query = select("?movie")
                .where(
                        tripletOf("?movie", IS, SchemaLinker.Movie.type),
                        hasUri("?movie", defaultResourcePath + uri)
                ).limit(1)
                .build();

        List<List<Supplier<Movie>>> resultSet = fetchAndTransform(query);
        if(resultSet.isEmpty()) return Optional.empty();
        return Optional.of(resultSet.get(0).get(0).get());
    }

    @Override
    public List<Movie> findAll() {
        List<Movie> movies = new ArrayList<>();
        fetchAndTransform(
                select("?movie")
                .where(
                        tripletOf("?movie", IS, SchemaLinker.Movie.type)
                ).limit(20).build()
        ).forEach(row -> row.forEach(lazyMove -> movies.add(lazyMove.get())));
        return movies;
    }

    @Override
    public List<Movie> findByName(String name) {
        List<Movie> movies = new ArrayList<>();
        fetchAndTransform(
                select("?movie")
                        .where(
                                tripletOf("?movie", IS, SchemaLinker.Movie.type),
                                tripletOf("?movie", SchemaLinker.Movie.hasName, "?name"),
                                like("?name", "^" + name)
                        ).build()
        ).forEach(row -> row.forEach(lazyMove -> movies.add(lazyMove.get())));
        return movies;
    }

    @Override
    public Movie hydrate(Document document) {

        String frenchName = orNull(() -> document.getElementsByTag("rdfs:label").stream().filter(it -> it.attr("xml:lang").equals("fr")).collect(Collectors.toList()).get(0).text());
        String englishName = orNull(() -> document.getElementsByTag("rdfs:label").stream().filter(it -> it.attr("xml:lang").equals("en")).collect(Collectors.toList()).get(0).text());
        Double gross = orNull(() -> Double.valueOf(document.getElementsByTag("dbo:gross").get(0).text().replace("E", "E+")));
        Double budget = orNull(() -> Double.valueOf(document.getElementsByTag("dbo:budget").get(0).text().replace("E", "E+")));
        String uri = document.baseUri();
        String enSynopsis = orNull(() -> document.getElementsByTag("dbo:abstract").stream().filter(it -> it.attr("xml:lang").equals("en")).collect(Collectors.toList()).get(0).text());
        String frSynopsis = orNull(() -> document.getElementsByTag("dbo:abstract").stream().filter(it -> it.attr("xml:lang").equals("fr")).collect(Collectors.toList()).get(0).text());

        return new Movie(uri,englishName, frenchName, gross, budget, enSynopsis, frSynopsis);
    }
}
