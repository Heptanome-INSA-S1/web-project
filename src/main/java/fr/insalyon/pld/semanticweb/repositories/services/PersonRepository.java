package fr.insalyon.pld.semanticweb.repositories.services;

import fr.insalyon.pld.semanticweb.repositories.AbstractSPARQLRepositoryImpl;
import fr.insalyon.pld.semanticweb.repositories.SPARQLRepository;
import fr.insalyon.pld.semanticweb.repositories.entities.utils.MultiSourcedDocument;
import fr.insalyon.pld.semanticweb.repositories.entities.Person;

import java.util.List;
import java.util.Optional;

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
