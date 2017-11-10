package fr.insalyon.pld.semanticweb.repositories.entities;

import fr.insalyon.pld.semanticweb.repositories.entities.utils.MultiSourcedLink;
import fr.insalyon.pld.semanticweb.repositories.entities.utils.URI;

import java.util.List;

public class Person {

  public List<URI> uri;
  public final String name;
  public final String firstName;
  public final String birthDate;
  public final String deathDate;
  public final String biography;
  public final List<URI> children;
  public final URI partner;

  public Person(List<URI> uri, String name, String firstName, String birthDate, String deathDate, String biography, List<URI> children, URI partner) {
    this.uri = uri;
    this.name = name;
    this.firstName = firstName;
    this.birthDate = birthDate;
    this.deathDate = deathDate;
    this.biography = biography;
    this.children = children;
    this.partner = partner;
  }

}
