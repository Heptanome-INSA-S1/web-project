package fr.insalyon.pld.semanticweb.repositories;

import fr.insalyon.pld.semanticweb.entities.MultiSourcedDocument;
import fr.insalyon.pld.semanticweb.entities.Person;
import fr.insalyon.pld.semanticweb.entities.URI;
import fr.insalyon.pld.semanticweb.model.persistence.SchemaLinker;
import fr.insalyon.pld.semanticweb.services.sparqldsl.QueryBuilder;
import org.jsoup.nodes.Document;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static fr.insalyon.pld.semanticweb.model.persistence.SchemaLinker.IS;
import static fr.insalyon.pld.semanticweb.model.tuple.Triplet.tripletOf;
import static fr.insalyon.pld.semanticweb.services.sparqldsl.Filter.hasUri;
import static fr.insalyon.pld.semanticweb.services.sparqldsl.QueryBuilderImpl.select;

public class PersonRepository extends AbstractSPARQLRepositoryImpl<Person> implements SPARQLRepository<Person> {

    @Override
    public Optional<Person> findById(String uri) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Person> findAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Person> findByName(String name) {
       throw new UnsupportedOperationException();
    }

    @Override
    public Person hydrate(MultiSourcedDocument document) {
        return null;
    }
}
