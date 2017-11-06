package fr.insalyon.pld.semanticweb.entities;

import java.util.List;
import java.util.Set;

public class Movie implements Model {

    public final String uri;
    public final String frenchName;
    public final String englishName;
    public final Double gross;
    public final Double budget;
    public final String englishSynopsis;
    public final String frenchSynopsis;

    public Movie(String uri, String englishName, String frenchName, Double gross, Double budget, String englishSynopsis, String frenchSynopsis) {
        this.uri = uri;
        this.frenchName = frenchName;
        this.englishName = englishName;
        this.gross = gross;
        this.budget = budget;
        this.englishSynopsis = englishSynopsis;
        this.frenchSynopsis = frenchSynopsis;
    }
}
