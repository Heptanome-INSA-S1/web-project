package fr.insalyon.pld.semanticweb.entities;

import java.util.Date;
import java.util.List;

public class Person {

    public final String name;
    public final String firstName;
    public final String birthDate;
    public final String deathDate;
    public final String biography;
    public final List<URI> children;
    public final URI partner;

    public Person(String name, String firstName, String birthDate, String deathDate, String biography, List<URI> children, URI partner) {
        this.name = name;
        this.firstName = firstName;
        this.birthDate = birthDate;
        this.deathDate = deathDate;
        this.biography = biography;
        this.children = children;
        this.partner = partner;
    }

}
