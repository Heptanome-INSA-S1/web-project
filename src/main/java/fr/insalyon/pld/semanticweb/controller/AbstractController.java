package fr.insalyon.pld.semanticweb.controller;

import fr.insalyon.pld.semanticweb.repositories.SPARQLRepository;
import fr.insalyon.pld.semanticweb.repositories.entities.Movie;
import fr.insalyon.pld.semanticweb.repositories.mapper.Mapper;

import java.util.List;
import java.util.Optional;

public class AbstractController {

  protected <E, M> List<M> fetchFromRepository(String query, String isShort, SPARQLRepository<E> repository, Mapper<E, M> mapper) {
    List<M> result;
    if (null == query) {
      if("true".equals(isShort)) {
        result = mapper.entitiesToLightModels(repository.findAll());
      } else {
        result = mapper.entitiesToFullModels(repository.findAll());
      }
    } else {
      if("true".equals(isShort)) {
        result = mapper.entitiesToLightModels(repository.findByName(query));
      } else {
        result = mapper.entitiesToFullModels(repository.findByName(query));
      }
    }
    return result;
  }

  protected <E, M> M fetchUniqueFromRepository(String uuid, SPARQLRepository<E> repository, Mapper<E, M> mapper) {
    Optional<E> optional = repository.findById(uuid);

    if (optional.isPresent()) {
      return mapper.entityToFullModel(optional.get());
    } else {
      throw new RuntimeException("Resource not found");
    }
  }

}
