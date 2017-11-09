package fr.insalyon.pld.semanticweb.repositories.services;

import fr.insalyon.pld.semanticweb.repositories.AbstractSPARQLRepositoryImpl;
import fr.insalyon.pld.semanticweb.repositories.SPARQLRepository;
import fr.insalyon.pld.semanticweb.repositories.entities.Movie;
import fr.insalyon.pld.semanticweb.repositories.entities.utils.MultiSourcedDocument;
import fr.insalyon.pld.semanticweb.repositories.entities.utils.URI;
import fr.insalyon.pld.semanticweb.model.persistence.SchemaLinker;
import fr.insalyon.pld.semanticweb.services.sparqldsl.QueryBuilder;
import fr.insalyon.pld.semanticweb.util.Lazy;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static fr.insalyon.pld.semanticweb.model.persistence.SchemaLinker.IS;
import static fr.insalyon.pld.semanticweb.model.tuple.Triplet.tripletOf;
import static fr.insalyon.pld.semanticweb.services.sparqldsl.Filter.hasUri;
import static fr.insalyon.pld.semanticweb.services.sparqldsl.Filter.like;
import static fr.insalyon.pld.semanticweb.services.sparqldsl.QueryBuilderImpl.select;

@Component("MovieRepository")
public class MovieRepository extends AbstractSPARQLRepositoryImpl<Movie> implements SPARQLRepository<Movie> {

  @Override
  public List<Movie> findAll() {
    List<Movie> movies = new ArrayList<>();
    fetchAndTransform(
        select("?movie")
            .where(
                tripletOf("?movie", IS, SchemaLinker.Movie.type)
            ).orderAsc("?movie").limit(5)
    ).forEach(row -> row.forEach(lazyMovie -> movies.add(lazyMovie.get())));
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
  public Movie hydrate(MultiSourcedDocument document) {
    String poster = null; // pas trouvé sur dbpedia, TODO
    String title = getTextOrderByLang(document.get(URI.Database.DBPEDIA), "rdfs:label", Arrays.asList("fr", "en"));
    String releaseDate = orNull(() ->
        document
            .get(URI.Database.LINKED_MDB)
            .getElementsByTag("a").stream()
            .filter(link -> link.attr("href").endsWith("initial_release_date"))
            .collect(Collectors.toList())
            .get(0).parent().nextElementSibling().getElementsByTag("span").get(0).text()
    );
    Double gross = orNull(() -> Double.valueOf(document.get(URI.Database.DBPEDIA).getElementsByTag("dbo:gross").get(0).text().replace("E", "E+")));
    Double budget = orNull(() -> Double.valueOf(document.get(URI.Database.DBPEDIA).getElementsByTag("dbo:budget").get(0).text().replace("E", "E+")));

    String synopsis = getTextOrderByLang(document.get(URI.Database.DBPEDIA), "dbo:abstract", Arrays.asList("fr", "en"));

    Double runtime = orNull(() -> Double.valueOf(document.get(URI.Database.DBPEDIA).getElementsByTag("dbo:runtime").get(0).text().replace("E", "E+")));
    List<URI> actors = orEmpty(() -> extractResourceFrom(document.get(URI.Database.DBPEDIA), "dbo:starring"));
    List<URI> directors = orEmpty(() -> extractResourceFrom(document.get(URI.Database.DBPEDIA), "dbo:director"));

    directors.addAll(
        orEmpty(() ->
            document
                .get(URI.Database.LINKED_MDB)
                .getElementsByTag("a").stream()
                .filter(link -> link.attr("href").endsWith("movie/producer"))
                .map(element -> URI.from(element.parent().nextElementSibling().getElementsByTag("a").get(0).attr("href")))
                .collect(Collectors.toList())
        )
    );

    List<String> genres = orEmpty(() -> null);

    URI dbpediaURI = orNull(() -> URI.from(document.get(URI.Database.DBPEDIA).baseUri()));
    URI linkedmdbURI = orNull(() -> URI.from(document.get(URI.Database.LINKED_MDB).baseUri()));
    List<URI> sources = new ArrayList<>();

    if (dbpediaURI != null) sources.add(dbpediaURI);
    if (linkedmdbURI != null) sources.add(linkedmdbURI);

    return new Movie(sources, poster, title, releaseDate, synopsis, runtime, actors, genres, directors, gross, budget);
  }
}
