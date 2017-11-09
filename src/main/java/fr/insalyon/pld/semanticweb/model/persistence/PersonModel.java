package fr.insalyon.pld.semanticweb.model.persistence;

import java.util.List;

public class PersonModel {

    public final String name;
    public final String firstName;
    public final String birthDate;
    public final String deathDate;
    public final String biography;
    public final List<PersonModel> children;
    public final PersonModel partner;

    public PersonModel(String name, String firstName, String birthDate, String deathDate, String biography, List<PersonModel> children, PersonModel partner) {
        this.name = name;
        this.firstName = firstName;
        this.birthDate = birthDate;
        this.deathDate = deathDate;
        this.biography = biography;
        this.children = children;
        this.partner = partner;
    }
}
