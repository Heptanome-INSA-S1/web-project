package fr.insalyon.pld.semanticweb.repositories;

import fr.insalyon.pld.semanticweb.entities.Movie;
import fr.insalyon.pld.semanticweb.model.persistence.SchemaLinker;
import fr.insalyon.pld.semanticweb.services.sparqldsl.QueryBuilder;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static fr.insalyon.pld.semanticweb.model.persistence.SchemaLinker.IS;
import static fr.insalyon.pld.semanticweb.model.tuple.Triplet.tripletOf;
import static fr.insalyon.pld.semanticweb.services.sparqldsl.Filter.hasUri;
import static fr.insalyon.pld.semanticweb.services.sparqldsl.Filter.like;
import static fr.insalyon.pld.semanticweb.services.sparqldsl.QueryBuilderImpl.select;

@Component("MovieRepository")
public class MovieRepository implements SPARQLRepository<Movie> {

    @Override
    public Optional<Movie> findById(String uri) {

        QueryBuilder query = select("?movie")
                .where(
                        tripletOf("?movie", IS, SchemaLinker.Movie.type),
                        hasUri("?movie", HTTP_DBPEDIA_ORG + "page/" + uri)
                ).union(
                        tripletOf("?movie", IS, SchemaLinker.Movie.type),
                        hasUri("?movie", HTTP_DATA_LINKEDMDB_ORG + "resource/film/" + uri)
                )
                .limit(1);

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
                ).orderAsc("?movie").limit(5)
        ).forEach(row -> row.forEach(lazyMove -> movies.add(lazyMove.get())));
        return movies;
    }

    @Override
    public List<Movie> findByName(String name) {
        List<Movie> movies = new ArrayList<>();
        fetchAndTransform(
                select("?movie, ?actor")
                        .where(
                                tripletOf("?movie", IS, SchemaLinker.Movie.type),
                                tripletOf("?movie", SchemaLinker.Movie.hasName, "?name"),
                                like("?name", "^" + name)
                        )
        ).forEach(row -> row.forEach(lazyMove -> movies.add(lazyMove.get())));
        return movies;
    }

    @Override
    public Movie hydrate(Document document) {

        String frenchName = orNull(() -> getElementByFilteredTag(document, "rdfs:label", "xml:lang", "fr").text());
        String englishName = orNull(() -> getElementByFilteredTag(document, "rdfs:label", "xml:lang", "en").text());
        Double gross = orNull(() -> Double.valueOf(document.getElementsByTag("dbo:gross").get(0).text().replace("E", "E+")));
        Double budget = orNull(() -> Double.valueOf(document.getElementsByTag("dbo:budget").get(0).text().replace("E", "E+")));
        String uri = document.baseUri();
        String enSynopsis = orNull(() -> getElementByFilteredTag(document, "dbo:abstract", "xml:lang", "fr").text());
        String frSynopsis = orNull(() -> getElementByFilteredTag(document, "dbo:abstract", "xml:lang", "fr").text());

        return new Movie(uri,englishName, frenchName, gross, budget, enSynopsis, frSynopsis);
    }
}
