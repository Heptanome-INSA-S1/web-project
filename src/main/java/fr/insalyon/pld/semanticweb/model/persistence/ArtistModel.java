package fr.insalyon.pld.semanticweb.model.persistence;

import fr.insalyon.pld.semanticweb.repositories.entities.Movie;
import fr.insalyon.pld.semanticweb.repositories.entities.utils.URI;

import java.util.List;

public class ArtistModel extends PersonModel {

  public final List<Movie> filmography;
  public final List<Movie> bestMovies;

  public ArtistModel(
      List<URI> uri,
      String name,
      String firstName,
      String birthDate,
      String deathDate,
      String biography,
      List<PersonModel> children,
      PersonModel partner,
      List<Movie> filmography,
      List<Movie> bestMovies
  ) {
    super(uri, name, firstName, birthDate, deathDate, biography, children, partner);
    this.filmography = filmography;
    this.bestMovies = bestMovies;
  }
}
