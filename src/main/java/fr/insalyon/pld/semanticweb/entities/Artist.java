package fr.insalyon.pld.semanticweb.entities;

import java.util.List;

public class Artist {

    public final Personne information;
    public final List<Movie> filmography;
    public final List<Movie> bestMovies;

    public Artist(Personne information, List<Movie> filmography, List<Movie> bestMovies){
        this.information = information;
        this.filmography = filmography;
        this.bestMovies = bestMovies;
    }


}
