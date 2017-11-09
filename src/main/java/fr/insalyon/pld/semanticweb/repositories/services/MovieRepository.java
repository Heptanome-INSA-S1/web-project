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
    String title = orNull(
        () -> getTextOrderByLang(document.get(URI.Database.DBPEDIA), "rdfs:label", Arrays.asList("fr", "en")),
        () -> document.get(URI.Database.LINKED_MDB).getElementsByTag("rdfs:label").get(0).text()
    );
    String releaseDate = orNull(() -> document.get(URI.Database.LINKED_MDB).getElementsByTag("movie:initial_release_date").get(0).text());
    Double gross = orNull(() -> Double.valueOf(document.get(URI.Database.DBPEDIA).getElementsByTag("dbo:gross").get(0).text().replace("E", "E+")));
    Double budget = orNull(() -> Double.valueOf(document.get(URI.Database.DBPEDIA).getElementsByTag("dbo:budget").get(0).text().replace("E", "E+")));

    String synopsis = orNull(() -> getTextOrderByLang(document.get(URI.Database.DBPEDIA), "dbo:abstract", Arrays.asList("fr", "en")));

    Double runtime = orNull(
        () -> Double.valueOf(document.get(URI.Database.DBPEDIA).getElementsByTag("dbo:runtime").get(0).text()),
        () -> Double.valueOf(document.get(URI.Database.LINKED_MDB).getElementsByTag("dbo:runtime").get(0).text())
    );
    List<URI> actors = orEmpty(
        () -> extractResourceFrom(document.get(URI.Database.DBPEDIA), "dbo:starring"),
        () -> extractResourceFrom(document.get(URI.Database.LINKED_MDB), "movie:actor")
    );
    List<URI> directors = orEmpty(
        () -> extractResourceFrom(document.get(URI.Database.DBPEDIA), "dbo:director")
    // todo    () -> extractResourceFrom(document.get(URI.Database.LINKED_MDB), "movie:director")
    );

    directors.addAll(
        extractResourceFrom(document.get(URI.Database.LINKED_MDB), "movie:producer")
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
