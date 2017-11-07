package fr.insalyon.pld.semanticweb.entities;

public class Personne {

    public final String name;
    public final String birthDay;
    public final String deathDay;

    public Personne(String name, String birthDay, String deathDay){
        this.name = name;
        this.birthDay = birthDay;
        this.deathDay = deathDay;
    }

    public Personne(String name, String birthDay){
        this.name = name;
        this.birthDay = birthDay;
        this.deathDay = null;
    }
}
