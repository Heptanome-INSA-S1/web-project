package fr.insalyon.pld.semanticweb.entities;

import java.util.Date;

public class Personne {
    private Date birthDate;
    private Label[] labels;

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Label[] getLabels() {
        return labels;
    }

    public void setLabels(Label[] labels) {
        this.labels = labels;
    }
}
