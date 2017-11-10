package fr.insalyon.pld.semanticweb.repositories.entities;

import fr.insalyon.pld.semanticweb.repositories.entities.utils.MultiSourcedLink;
import fr.insalyon.pld.semanticweb.repositories.entities.utils.URI;

import java.util.List;

public class Artist extends Person {

  public final List<URI> filmography;
  public final List<URI> bestMovies;

  public Artist(
      List<URI> uri,
      String name,
      String firstName,
      String birthDate,
      String deathDate,
      String biography,
      List<URI> children,
      URI partner,
      List<URI> filmography,
      List<URI> bestMovies
  ) {
    super(uri, name, firstName, birthDate, deathDate, biography, children, partner);
    this.filmography = filmography;
    this.bestMovies = bestMovies;
  }
}
