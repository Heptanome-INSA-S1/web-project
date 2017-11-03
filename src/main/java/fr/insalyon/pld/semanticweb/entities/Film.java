package fr.insalyon.pld.semanticweb.entities;

import java.time.Duration;
import java.util.Date;

public class Film {
    private Label[] name;
    private double budget;
    private double gross;
    private Date publicationDate;
    private Personne[] actors;
    private Personne[] realisators;
    private Personne[] productors;
    private String synopsis;
    private Duration duration;
    private String type; //String ou class Type ??

    public Label[] getName() {
        return name;
    }

    public void setName(Label[] name) {
        this.name = name;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public double getGross() {
        return gross;
    }

    public void setGross(double gross) {
        this.gross = gross;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Personne[] getActors() {
        return actors;
    }

    public void setActors(Personne[] actors) {
        this.actors = actors;
    }

    public Personne[] getRealisators() {
        return realisators;
    }

    public void setRealisators(Personne[] realisators) {
        this.realisators = realisators;
    }

    public Personne[] getProductors() {
        return productors;
    }

    public void setProductors(Personne[] productors) {
        this.productors = productors;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
}
