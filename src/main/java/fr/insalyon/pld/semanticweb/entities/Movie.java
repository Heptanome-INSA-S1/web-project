package fr.insalyon.pld.semanticweb.entities;

import java.util.Date;
import java.util.List;

public class Movie extends Work {

    public Movie(List<URI> uri, String poster, String title, Date releaseDate, String plot, Double runtime, List<URI> artists, List<URI> genres, List<URI> directors, Double gross, Double budget) {
        super(uri, poster, title, releaseDate, plot, runtime, artists, genres, directors, gross, budget);
    }
}
