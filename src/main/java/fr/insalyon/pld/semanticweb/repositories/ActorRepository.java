package fr.insalyon.pld.semanticweb.repositories;

import fr.insalyon.pld.semanticweb.entities.Artist;
import fr.insalyon.pld.semanticweb.entities.Personne;
import fr.insalyon.pld.semanticweb.entities.URI;
import fr.insalyon.pld.semanticweb.model.persistence.SchemaLinker;
import fr.insalyon.pld.semanticweb.services.sparqldsl.QueryBuilder;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

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

@Component("ActorRepository")
public class ActorRepository implements SPARQLRepository<Artist> {

    @Override
    public Optional<Artist> findById(String uri) {
        QueryBuilder query = select("?actor")
                .where(
                        tripletOf("?actor", IS, SchemaLinker.Actor.type),
                        hasUri("?actor", HTTP_DBPEDIA_ORG + "page/" + uri)
                ).union(
                        tripletOf("?actor", IS, SchemaLinker.Movie.type),
                        hasUri("?actor", HTTP_DATA_LINKEDMDB_ORG + "directory/actor" + uri)
                ).limit(1);
        List<List<Supplier<Artist>>> resultSet = fetchAndTransform(query);

        if(resultSet.isEmpty()) return Optional.empty();
        return Optional.of(resultSet.get(0).get(0).get());
    }

    @Override
    public List<Artist> findAll() {
        List<Artist> actors = new ArrayList<>();
        fetchAndTransform(
                select("?actor")
                        .where(
                                tripletOf("?actor", IS, SchemaLinker.Actor.type)
                        ).limit(20)
        ).forEach(row -> row.forEach(lazyMove -> actors.add(lazyMove.get())));
        return actors;
    }

    @Override
    public List<Artist> findByName(String name) {
        List<Artist> actors = new ArrayList<>();
        fetchAndTransform(
                select("?actor")
                        .where(
                                tripletOf("?actor", IS, SchemaLinker.Actor.type),
                                tripletOf("?actor", SchemaLinker.Actor.hasName, "?name"),
                                like("?name", "" + name)
                        )
        ).forEach(row -> row.forEach(lazyMove -> actors.add(lazyMove.get())));
        return actors;
    }

    @Override
    public Artist hydrate(Document document) {
        String name = orNull(() -> document.getElementsByTag("foaf:name").get(0).text());
        String birthDay = orNull(() -> document.getElementsByTag("dbo:birthDate").get(0).text());
        String deathDay = orNull(() -> document.getElementsByTag("dbo:deathDate").get(0).text());
        List<URI> movies = orEmpty(() -> document.getElementsByTag("dbo:starring").parents().stream().map(element -> URI.from(element.attr("rdf:about"))).collect(Collectors.toList()));
        List<URI> bestMovies = orEmpty(() -> new ArrayList<URI>(movies.subList(0,3)));

        Personne p = new Personne(name, birthDay,deathDay);
        return new Artist(p , movies, bestMovies);
    }
}
