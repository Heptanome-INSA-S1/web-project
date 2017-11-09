package fr.insalyon.pld.semanticweb.repositories.entities;

import fr.insalyon.pld.semanticweb.repositories.entities.utils.MultiSourcedLink;
import fr.insalyon.pld.semanticweb.repositories.entities.utils.URI;

import java.util.Date;
import java.util.List;

public class Movie extends Work {

    public Movie(List<URI> uri, String poster, String title, String releaseDate, String plot, Double runtime, List<URI> artists, List<String> genres, List<URI> directors, Double gross, Double budget) {
        super(uri, poster, title, releaseDate, plot, runtime, artists, genres, directors, gross, budget);
    }
}
