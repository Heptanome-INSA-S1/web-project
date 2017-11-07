package fr.insalyon.pld.semanticweb.repositories;

import fr.insalyon.pld.semanticweb.entities.Movie;
import fr.insalyon.pld.semanticweb.entities.URI;
import fr.insalyon.pld.semanticweb.model.persistence.SchemaLinker;
import fr.insalyon.pld.semanticweb.services.sparqldsl.QueryBuilder;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
                select("?movie")
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
        String poster = null; // pas trouvé sur dbpedia, TODO
        
        String frenchName = orNull(() -> document.getElementsByTag("rdfs:label").stream().filter(it -> it.attr("xml:lang").equals("fr")).collect(Collectors.toList()).get(0).text());
        String englishName = orNull(() -> document.getElementsByTag("rdfs:label").stream().filter(it -> it.attr("xml:lang").equals("en")).collect(Collectors.toList()).get(0).text());
        String title = (frenchName == null ? englishName : frenchName);
        
        Date releaseDate = null; // pas trouvé sur dbpedia, TODO
        
        Double gross = orNull(() -> Double.valueOf(document.getElementsByTag("dbo:gross").get(0).text().replace("E", "E+")));
        
        Double budget = orNull(() -> Double.valueOf(document.getElementsByTag("dbo:budget").get(0).text().replace("E", "E+")));
        
        String uri = document.baseUri();
        
        String enSynopsis = orNull(() -> document.getElementsByTag("dbo:abstract").stream().filter(it -> it.attr("xml:lang").equals("en")).collect(Collectors.toList()).get(0).text());
        String frSynopsis = orNull(() -> document.getElementsByTag("dbo:abstract").stream().filter(it -> it.attr("xml:lang").equals("fr")).collect(Collectors.toList()).get(0).text());
        String plot = (frSynopsis == null ? enSynopsis : frSynopsis);
        
        Double runtime = orNull(() -> Double.valueOf(document.getElementsByTag("dbo:runtime").get(0).text().replace("E", "E+")));
        
        List<URI> actors = orEmpty(() -> document.getElementsByTag("dbo:starring").stream().map(element -> URI.from(element.attr("rdf:resource"))).collect(Collectors.toList()));

        List<URI> directors = orEmpty(() -> document.getElementsByTag("dbo:sdirector").stream().map(element -> URI.from(element.attr("rdf:resource"))).collect(Collectors.toList()));

        List<URI> genres = orEmpty(() -> document.getElementsByTag("dbo:genre").stream().map(element -> URI.from(element.attr("rdf:resource"))).collect(Collectors.toList()));

        return new Movie(uri, poster, title, releaseDate, plot, runtime, actors, genres, directors, gross, budget);
    }
}
