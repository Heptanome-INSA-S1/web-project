package fr.insalyon.pld.semanticweb.repositories.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public interface Mapper<E, M> {

  default M entityToLightModel(E entity) {
    return entityToLightModel(entity, true);
  }
  M entityToLightModel(E entity, Boolean firstCall);

  M entityToFullModel(E entity);

  default List<M> entitiesToLightModels(List<E> entities, Boolean firstCall) {
    return entities.stream()
            .filter(Objects::nonNull)
            .map(entity -> entityToLightModel(entity, firstCall))
            .collect(Collectors.toList());
  }

  default List<M> entitiesToLightModels(List<E> entities) {
    return entities.stream()
        .filter(Objects::nonNull)
        .map(this::entityToLightModel)
        .collect(Collectors.toList());
  }

  default List<M> entitiesToFullModels(List<E> entities) {
    return entities.stream()
            .filter(Objects::nonNull)
            .map(this::entityToLightModel)
            .collect(Collectors.toList());
  }

  default <E> List<E> orEmpty(Supplier<List<E>> supplier) {
    List<E> result = new ArrayList<>();
      try {
        result.addAll(supplier.get());
      } catch (Exception ignored) {}
    return result;
  }

  default  <E> E orNull(Supplier<E> supplier) {
    try {
      return supplier.get();
    } catch (Exception ignored) {
      return null;
    }
  }

}
