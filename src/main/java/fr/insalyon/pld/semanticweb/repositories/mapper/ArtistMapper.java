package fr.insalyon.pld.semanticweb.repositories.mapper;

import fr.insalyon.pld.semanticweb.repositories.entities.Artist;
import fr.insalyon.pld.semanticweb.model.persistence.ArtistModel;
import fr.insalyon.pld.semanticweb.repositories.entities.Person;
import fr.insalyon.pld.semanticweb.repositories.services.ActorRepository;
import fr.insalyon.pld.semanticweb.repositories.services.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component("ArtistMapper")
public class ArtistMapper implements Mapper<Artist, ArtistModel> {

  @Autowired
  MovieRepository movieRepository;

  @Autowired
  ActorRepository actorRepository;

  PersonMapper personMapper = new PersonMapper();

  @Override
  public ArtistModel entityToLightModel(Artist entity) {

    return new ArtistModel(
        entity.uri,
        entity.name,
        entity.firstName,
        entity.birthDate,
        entity.deathDate,
        entity.biography,
        new ArrayList<>(),
        null,
        new ArrayList<>(),
        new ArrayList<>()
    );

  }

  @Override
  public ArtistModel entityToFullModel(Artist entity) {
    return new ArtistModel(
        entity.uri,
        entity.name,
        entity.firstName,
        entity.birthDate,
        entity.deathDate,
        entity.biography,
        personMapper.entitiesToLightModels(actorRepository.retrieveFromURI(entity.children).stream().map(child -> (Person)child).collect(Collectors.toList())),
        orNull(() -> personMapper.entityToLightModel(actorRepository.retrieveFromURI(Arrays.asList(entity.partner)).stream().map(partner -> (Person)partner).collect(Collectors.toList()).get(0))),
        movieRepository.retrieveFromURI(entity.filmography),
        movieRepository.retrieveFromURI(entity.bestMovies)
    );
  }

  private <E> E orNull(Supplier<E> supplier) {
    try {
      return supplier.get();
    } catch (Exception ignored) {
      return null;
    }
  }

}
