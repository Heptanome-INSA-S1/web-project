package fr.insalyon.pld.semanticweb.repositories.mapper;

import fr.insalyon.pld.semanticweb.model.persistence.PersonModel;
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
  MovieMapper movieMapper = new MovieMapper();

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
        movieMapper.entitiesToLightModels(movieRepository.retrieveFromURI(entity.filmography)),
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
        orNull(
            () -> personMapper.entityToLightModel(actorRepository.retrieveFromURI(entity.partner))
        ),
        movieMapper.entitiesToLightModels(movieRepository.retrieveFromURI(entity.filmography)),
        movieMapper.entitiesToLightModels(movieRepository.retrieveFromURI(entity.bestMovies))
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
