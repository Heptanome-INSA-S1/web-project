package fr.insalyon.pld.semanticweb.entities;

import java.net.URI;
import java.util.List;

public class Artist {

    public final Personne information;
    public final List<URI> filmography;
    public final List<URI> bestMovies;

    public Artist(Personne information, List<URI> filmography, List<URI> bestMovies){
        this.information = information;
        this.filmography = filmography;
        this.bestMovies = bestMovies;
    }

}
