package fr.insalyon.pld.semanticweb.model.persistence;

import fr.insalyon.pld.semanticweb.entities.Movie;

import java.util.List;

public class ArtistModel extends PersonModel {

    public final List<Movie> filmography;
    public final List<Movie> bestMovies;

    public ArtistModel(
            String name,
            String firstName,
            String birthDate,
            String deathDate,
            String biography,
            List<PersonModel> children,
            PersonModel partner,
            List<Movie> filmography,
            List<Movie> bestMovies
    ) {
        super(name, firstName, birthDate, deathDate, biography, children, partner);
        this.filmography = filmography;
        this.bestMovies = bestMovies;
    }
}
