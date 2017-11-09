package fr.insalyon.pld.semanticweb.repositories.mapper;

import fr.insalyon.pld.semanticweb.entities.Person;
import fr.insalyon.pld.semanticweb.model.persistence.PersonModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class PersonMapper implements Mapper<Person, PersonModel> {

    @Override
    public PersonModel entityToLightModel(Person entity) {
        return new PersonModel(
                entity.name,
                entity.firstName,
                entity.birthDate,
                entity.deathDate,
                entity.biography,
                new ArrayList<>(),
                null
        );
    }

    @Override
    public PersonModel entityToFullModel(Person entity) {

        return null;

    }
}
