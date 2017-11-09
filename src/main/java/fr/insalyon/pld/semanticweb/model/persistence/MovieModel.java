package fr.insalyon.pld.semanticweb.model.persistence;

import fr.insalyon.pld.semanticweb.repositories.entities.utils.URI;

import java.util.List;

public class MovieModel extends WorkModel {

  public MovieModel(
      List<URI> uri,
      String poster,
      String releaseDate,
      String plot,
      List<ArtistModel> actors,
      List<String> genres,
      List<ArtistModel> directors,
      Double gross,
      Double budget
  ) {
    super(uri, poster, releaseDate, plot, actors, genres, directors, gross, budget);
  }
}
