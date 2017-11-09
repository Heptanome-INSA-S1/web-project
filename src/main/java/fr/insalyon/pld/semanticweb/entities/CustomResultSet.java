package fr.insalyon.pld.semanticweb.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CustomResultSet<E> extends ArrayList<ArrayList<E>> {

  private CustomResultSet() {
    super();
  }

  private CustomResultSet(List<List<E>> matrix) {
    super(matrix.size());
    matrix.forEach(row -> {
      add(new ArrayList<>());
      row.forEach(element -> {
        get(size() - 1).add(element);
      });
    });
  }

  public static <E> CustomResultSet<E> init(int row, int col) {
    CustomResultSet<E> resultSet = new CustomResultSet<>();
    for(int i = 0; i < row; i++) {
      resultSet.add(new ArrayList<>());
      for(int j = 0; j < col; j++) {
        resultSet.get(i).add(null);
      }
    }
    return resultSet;
  }

  public <U> CustomResultSet<U> map(Function<E, U> mapper) {
    return new CustomResultSet<>(
        stream()
            .map(row -> row.stream().map(mapper::apply).collect(Collectors.toList()))
            .collect(Collectors.toList()));
  }

  public List<List<E>> toListOfList() {
    List<List<E>> result = new ArrayList<>(size());
    forEach(row -> {
      result.add(new ArrayList<>());
      row.forEach(element -> {
        result.get(result.size() - 1).add(element);
      });
    });
    return result;
  }

}
