package fr.insalyon.pld.semanticweb.entities;

import java.util.Date;

public class Personne {

    public final String name;
    public final String firstname;
    public final String birthDay;
    public final String deathDay;

    public Personne(String name, String firstname, String birthDay, String deathDay){
        this.name = name;
        this.firstname = firstname;
        this.birthDay = birthDay;
        this.deathDay = deathDay;
    }

    public Personne(String name, String firstname, String birthDay){
        this.name = name;
        this.firstname = firstname;
        this.birthDay = birthDay;
        this.deathDay = null;
    }
}
