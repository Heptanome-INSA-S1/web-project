package fr.insalyon.pld.semanticweb.entities;

import java.util.Date;
import java.util.List;

public class Movie extends Work {

    public Movie(String uri, String poster, String title, Date releaseDate, String plot, Double runtime, List<URI> artists, List<URI> genres, List<URI> directors, double gross, double budget) {
        super(uri, poster, title, releaseDate, plot, runtime, artists, genres, directors, gross, budget);
    }
}
