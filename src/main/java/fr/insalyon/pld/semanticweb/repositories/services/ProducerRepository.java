package fr.insalyon.pld.semanticweb.repositories.services;

import fr.insalyon.pld.semanticweb.model.persistence.SchemaLinker;
import fr.insalyon.pld.semanticweb.repositories.AbstractSPARQLRepositoryImpl;
import fr.insalyon.pld.semanticweb.repositories.SPARQLRepository;
import fr.insalyon.pld.semanticweb.repositories.entities.Artist;
import fr.insalyon.pld.semanticweb.repositories.entities.utils.MultiSourcedDocument;
import fr.insalyon.pld.semanticweb.repositories.entities.utils.URI;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static fr.insalyon.pld.semanticweb.model.persistence.SchemaLinker.IS;
import static fr.insalyon.pld.semanticweb.model.tuple.Triplet.tripletOf;
import static fr.insalyon.pld.semanticweb.services.sparqldsl.Filter.like;
import static fr.insalyon.pld.semanticweb.services.sparqldsl.QueryBuilderImpl.select;

@Component("ProducerRespository")
public class ProducerRepository extends AbstractSPARQLRepositoryImpl<Artist> implements SPARQLRepository<Artist> {

  @Override
  public List<Artist> findAll() {
    List<Artist> actors = new ArrayList<>();
    fetchAndTransform(
        select("?producer")
            .where(
                tripletOf("?producer", IS, SchemaLinker.Producer.type)
            ).limit(20)
    ).forEach(row -> row.forEach(lazyMove -> actors.add(lazyMove.get())));
    return actors;
  }

  @Override
  public List<Artist> findByName(String name) {
    List<Artist> actors = new ArrayList<>();
    fetchAndTransform(
        select("?producer")
            .where(
                tripletOf("?producer", IS, SchemaLinker.Producer.type),
                tripletOf("?producer", SchemaLinker.Producer.hasName, "?name"),
                like("?name", "" + name)
            )
    ).forEach(row -> row.forEach(lazyMove -> actors.add(lazyMove.get())));
    return actors;
  }

  @Override
  public Artist hydrate(MultiSourcedDocument document) {
    String fullname = orNull(
        () -> document.get(URI.Database.DBPEDIA).getElementsByTag("foaf:name").get(0).text(),
        () -> document.get(URI.Database.LINKED_MDB).getElementsByTag("rdfs:label").get(0).text()
    );
    String photo = orNull(() -> document.get(URI.Database.DBPEDIA).getElementsByTag("dbo:thumbnail").get(0).attr("rdf:resource"));
    String birthDay = orNull(() -> document.get(URI.Database.DBPEDIA).getElementsByTag("dbo:birthDate").get(0).text());
    String deathDay = orNull(() -> document.get(URI.Database.DBPEDIA).getElementsByTag("dbo:deathDate").get(0).text());
    List<URI> movies = orEmpty(
        () -> document.get(URI.Database.DBPEDIA).getElementsByTag("dbo:producer").stream().map(element -> URI.from(element.parent().attr("rdf:about"))).collect(Collectors.toList()),
        () -> document.get(URI.Database.LINKED_MDB).getElementsByTag("movie:producer").stream().map(element -> URI.from(element.parent().attr("rdf:about"))).collect(Collectors.toList())
    );
    List<URI> bestMovies = orEmpty(() -> new ArrayList<URI>(movies.subList(0, 3)));
    String biography = orNull(
        () -> getTextOrderByLang(document.get(URI.Database.DBPEDIA), "dbo:abstract", Arrays.asList("fr", "en"))
    );
    if(biography == null) {
      biography = orNull(() -> document.get(URI.Database.DBPEDIA).getElementsByTag("dbo:abstract").get(0).text());
    }

    List<URI> children = orEmpty(
        () -> document.get(URI.Database.DBPEDIA).getElementsByTag("dbo:child").stream().map(el -> URI.from(el.parent().attr("rdf:about"))).collect(Collectors.toList())
    );

    URI partner = orNull(
        () -> lastOf(
            document.get(URI.Database.DBPEDIA).getElementsByTag("dbo:spouse").stream().map(el -> URI.from(el.parent().attr("rdf:about"))).collect(Collectors.toList())
        ),
        () -> lastOf(
            document.get(URI.Database.DBPEDIA).getElementsByTag("dbo:partner").stream().map(el -> URI.from(el.parent().attr("rdf:about"))).collect(Collectors.toList())
        )
    );

    String lastname = orNull(() -> fullname.split(" ")[1]);
    String firstname = orNull(() -> fullname.split(" ")[0]);

    List<URI> uri = document.entrySet().stream().map(entry -> URI.from(document.get(entry.getKey()).baseUri())).collect(Collectors.toList());

    return new Artist(
        uri,
        lastname,
        firstname,
        photo,
        birthDay,
        deathDay,
        biography,
        children,
        partner,
        movies,
        bestMovies
    );

  }

  private <E> E lastOf(List<E> list) {
    if(list.isEmpty()) return null;
    else return list.get(list.size() - 1);
  }
}
