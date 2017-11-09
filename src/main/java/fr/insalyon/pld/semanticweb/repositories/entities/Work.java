package fr.insalyon.pld.semanticweb.repositories.entities;

import fr.insalyon.pld.semanticweb.repositories.entities.utils.URI;

import java.util.List;

public class Work {

    public List<URI> uri;
    public String poster;
    public String title;
    public String releaseDate;
    public String plot;
    public Double runtime;
    public List<URI> actors;
    public List<String> genres;
    public List<URI> directors;
    public Double gross;
    public Double budget;


    public Work(List<URI> uri, String poster, String title, String releaseDate, String plot, Double runtime, List<URI> actors, List<String> genres, List<URI> directors, Double gross, Double budget) {
        this.uri = uri;
        this.poster = poster;
        this.title = title;
        this.releaseDate = releaseDate;
        this.plot = plot;
        this.runtime = runtime;
        this.actors = actors;
        this.genres = genres;
        this.directors = directors;
        this.gross = gross;
        this.budget = budget;
    }
}
