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

import static fr.insalyon.pld.semanticweb.model.persistence.SchemaLinker.IS;
import static fr.insalyon.pld.semanticweb.model.tuple.Triplet.tripletOf;
import static fr.insalyon.pld.semanticweb.services.sparqldsl.Filter.hasUri;
import static fr.insalyon.pld.semanticweb.services.sparqldsl.Filter.like;
import static fr.insalyon.pld.semanticweb.services.sparqldsl.QueryBuilderImpl.select;

@Component("MovieRepository")
public class MovieRepository extends AbstractSPARQLRepositoryImpl<Movie> implements SPARQLRepository<Movie> {

  @Override
  public Optional<Movie> findById(String uri) {

    QueryBuilder query = select("?movie")
        .where(
            tripletOf("?movie", IS, SchemaLinker.Movie.type),
            hasUri("?movie", HTTP_DBPEDIA_ORG + "page/" + uri)
        ).union(
            tripletOf("?movie", IS, SchemaLinker.Movie.type),
            hasUri("?movie", HTTP_DATA_LINKEDMDB_ORG + "resource/film/" + uri)
        ).limit(1);

    List<List<Lazy<Movie>>> resultSet = fetchAndTransform(query);
    if (resultSet.isEmpty()) return Optional.empty();
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
    String frenchName = orNull(() -> getElementByFilteredTag(document.get(URI.Database.DBPEDIA), "rdfs:label", "xml:lang", "fr").text());
    String englishName = orNull(() -> getElementByFilteredTag(document.get(URI.Database.DBPEDIA), "rdfs:label", "xml:lang", "en").text());
    String title = (frenchName == null ? englishName : frenchName);
    String releaseDate = null; // pas trouvé sur dbpedia, TODO
    Double gross = orNull(() -> Double.valueOf(document.get(URI.Database.DBPEDIA).getElementsByTag("dbo:gross").get(0).text().replace("E", "E+")));
    Double budget = orNull(() -> Double.valueOf(document.get(URI.Database.DBPEDIA).getElementsByTag("dbo:budget").get(0).text().replace("E", "E+")));

    String enSynopsis = orNull(() -> getElementByFilteredTag(document.get(URI.Database.DBPEDIA), "dbo:abstract", "xml:lang", "fr").text());
    String frSynopsis = orNull(() -> getElementByFilteredTag(document.get(URI.Database.DBPEDIA), "dbo:abstract", "xml:lang", "en").text());
    String plot = (frSynopsis == null ? enSynopsis : frSynopsis);
    Double runtime = orNull(() -> Double.valueOf(document.get(URI.Database.DBPEDIA).getElementsByTag("dbo:runtime").get(0).text().replace("E", "E+")));
    List<URI> actors = orEmpty(() -> extractResourceFrom(document.get(URI.Database.DBPEDIA), "dbo:starring"));
    List<URI> directors = orEmpty(() -> extractResourceFrom(document.get(URI.Database.DBPEDIA), "dbo:director"));
    List<String> genres = orEmpty(() -> null);

    URI dbpediaURI = orNull(() -> URI.from(document.get(URI.Database.DBPEDIA).baseUri()));
    URI linkedmdbURI = orNull(() -> URI.from(document.get(URI.Database.LINKED_MDB).baseUri()));
    List<URI> sources = new ArrayList<>();
    if (dbpediaURI != null) sources.add(dbpediaURI);
    if (linkedmdbURI != null) sources.add(linkedmdbURI);

    return new Movie(sources, poster, title, releaseDate, plot, runtime, actors, genres, directors, gross, budget);
  }
}
