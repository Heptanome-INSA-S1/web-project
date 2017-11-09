package fr.insalyon.pld.semanticweb.repositories.mapper;

import java.util.List;
import java.util.stream.Collectors;

public interface Mapper<E, M> {

  M entityToLightModel(E entity);

  M entityToFullModel(E entity);

  default List<M> entitiesToLightModels(List<E> entities) {
    return entities.stream().map(this::entityToLightModel).collect(Collectors.toList());
  }

  default List<M> entitiesToHeavyModels(List<E> entities) {
    return entities.stream().map(this::entityToLightModel).collect(Collectors.toList());
  }

}
