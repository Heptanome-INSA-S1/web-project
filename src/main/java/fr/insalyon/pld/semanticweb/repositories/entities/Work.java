package fr.insalyon.pld.semanticweb.repositories.entities;

import fr.insalyon.pld.semanticweb.repositories.entities.utils.MultiSourcedLink;
import fr.insalyon.pld.semanticweb.repositories.entities.utils.URI;

import java.util.Date;
import java.util.List;

public class Work {

    public List<URI> uri;
    public String poster;
    public String title;
    public String releaseDate;
    public String plot;
    public Double runtime;
    public List<URI> artists;
    public List<String> genres;
    public List<URI> directors;
    public Double gross;
    public Double budget;


    public Work(List<URI> uri, String poster, String title, String releaseDate, String plot, Double runtime, List<URI> artists, List<String> genres, List<URI> directors, Double gross, Double budget) {
        this.uri = uri;
        this.poster = poster;
        this.title = title;
        this.releaseDate = releaseDate;
        this.plot = plot;
        this.runtime = runtime;
        this.artists = artists;
        this.genres = genres;
        this.directors = directors;
        this.gross = gross;
        this.budget = budget;
    }
}
