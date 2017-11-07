package fr.insalyon.pld.semanticweb.entities;

import java.net.URI;
import java.util.Date;
import java.util.List;

public class Work implements Model{

    public final String uri;
    public final String poster;
    public final String title;
    public final Date releaseDate;
    public final String plot;
    public final Double runtime;
    public final List<URI> artists;
    public final List<URI> genres;
    public final List<URI> directors;
    public final double gross;
    public final double budget;


    public Work(String uri, String poster, String title, Date releaseDate, String plot, int runtime, List<URI> artists, List<URI> genres, List<URI> directors, double gross, double budget) {
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
