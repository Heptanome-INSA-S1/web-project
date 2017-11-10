package fr.insalyon.pld.semanticweb.repositories.mapper;

import fr.insalyon.pld.semanticweb.model.persistence.MovieModel;
import fr.insalyon.pld.semanticweb.repositories.entities.Movie;
import fr.insalyon.pld.semanticweb.repositories.services.ActorRepository;
import fr.insalyon.pld.semanticweb.repositories.services.ProducerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Component("MovieMapper")
public class MovieMapper implements Mapper<Movie, MovieModel> {

  @Autowired
  ActorRepository actorRepository;

  @Autowired
  ProducerRepository producerRepository;

  @Autowired
  ArtistMapper artistMapper;

  @Override
  public MovieModel entityToLightModel(Movie entity, Boolean firstCall) {
    if(firstCall) {
      return new MovieModel(
          entity.uri,
          entity.title,
          entity.poster,
          entity.releaseDate,
          entity.plot,
          orEmpty(() -> artistMapper.entitiesToLightModels(actorRepository.retrieveFromURI(entity.actors), false)),
          entity.genres,
          new ArrayList<>(),
          entity.gross,
          entity.budget,
          entity.runtime,
          new ArrayList<>()
      );
    } else {
      return new MovieModel(
          entity.uri,
          entity.title,
          entity.poster,
          entity.releaseDate,
          entity.plot,
          new ArrayList<>(),
          entity.genres,
          new ArrayList<>(),
          entity.gross,
          entity.budget,
          entity.runtime,
          new ArrayList<>()
      );
    }
  }

  @Override
  public MovieModel entityToFullModel(Movie entity) {
    return new MovieModel(
        entity.uri,
        entity.title,
        entity.poster,
        entity.releaseDate,
        entity.plot,
        artistMapper.entitiesToLightModels(actorRepository.retrieveFromURI(entity.actors)),
        entity.genres,
        artistMapper.entitiesToLightModels(producerRepository.retrieveFromURI(entity.directors)),
        entity.gross,
        entity.budget,
        entity.runtime,
        artistMapper.entitiesToLightModels(producerRepository.retrieveFromURI(entity.writers))
    );
  }


}
