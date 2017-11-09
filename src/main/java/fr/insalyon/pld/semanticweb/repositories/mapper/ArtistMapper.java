package fr.insalyon.pld.semanticweb.repositories.mapper;

import fr.insalyon.pld.semanticweb.repositories.entities.Artist;
import fr.insalyon.pld.semanticweb.model.persistence.ArtistModel;
import fr.insalyon.pld.semanticweb.repositories.services.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component("ArtistMapper")
public class ArtistMapper implements Mapper<Artist, ArtistModel> {

  @Autowired
  MovieRepository movieRepository;

  @Override
  public ArtistModel entityToLightModel(Artist entity) {

    return new ArtistModel(
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
        entity.name,
        entity.firstName,
        entity.birthDate,
        entity.deathDate,
        entity.biography,
        new ArrayList<>(),
        null,
        movieRepository.retrieveFromURI(entity.filmography),
        movieRepository.retrieveFromURI(entity.bestMovies)
    );
  }
}
