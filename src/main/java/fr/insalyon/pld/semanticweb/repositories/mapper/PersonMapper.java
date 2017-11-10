package fr.insalyon.pld.semanticweb.repositories.mapper;

import fr.insalyon.pld.semanticweb.repositories.entities.Person;
import fr.insalyon.pld.semanticweb.model.persistence.PersonModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component("PersonMapper")
public class PersonMapper implements Mapper<Person, PersonModel> {

  @Override
  public PersonModel entityToLightModel(Person entity, Boolean firstCall) {
    if(firstCall) {
      return new PersonModel(
          entity.uri,
          entity.name,
          entity.firstName,
          entity.birthDate,
          entity.deathDate,
          entity.biography,
          new ArrayList<>(),
          null
      );
    } else {
      return new PersonModel(
          entity.uri,
          entity.name,
          entity.firstName,
          entity.birthDate,
          entity.deathDate,
          entity.biography,
          new ArrayList<>(),
          null
      );
    }
  }

  @Override
  public PersonModel entityToFullModel(Person entity) {

    return new PersonModel(
        entity.uri,
        entity.name,
        entity.firstName,
        entity.birthDate,
        entity.deathDate,
        entity.biography,
        new ArrayList<>(),
        null
    );

  }
}
