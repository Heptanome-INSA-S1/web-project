package fr.insalyon.pld.semanticweb.entities;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class Movie extends Work {

    public Movie(String uri, String poster, String title, Date releaseDate, String plot, int runtime, List<URI> artists, List<URI> genres, List<URI> directors, double gross, double budget) {
        super(uri, poster, title, releaseDate, plot, runtime, artists, genres, directors, gross, budget);
    }
}
