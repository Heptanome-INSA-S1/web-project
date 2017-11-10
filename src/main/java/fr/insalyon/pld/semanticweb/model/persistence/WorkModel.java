package fr.insalyon.pld.semanticweb.model.persistence;

import fr.insalyon.pld.semanticweb.repositories.entities.utils.URI;

import java.util.List;

public class WorkModel {

  public final List<URI> uri;
  public final String title;
  public final String poster;
  public final String releaseDate;
  public final String plot;
  public final List<ArtistModel> actors;
  public final List<String> genres;
  public final List<ArtistModel> directors;
  public final Double gross;
  public final Double budget;

  public WorkModel(List<URI> uri, String title, String poster, String releaseDate, String plot, List<ArtistModel> actors, List<String> genres, List<ArtistModel> directors, Double gross, Double budget) {
    this.uri = uri;
    this.title = title;
    this.poster = poster;
    this.releaseDate = releaseDate;
    this.plot = plot;
    this.actors = actors;
    this.genres = genres;
    this.directors = directors;
    this.gross = gross;
    this.budget = budget;
  }
}
