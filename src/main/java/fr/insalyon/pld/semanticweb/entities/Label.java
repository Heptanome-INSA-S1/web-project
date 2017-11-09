package fr.insalyon.pld.semanticweb.entities;

public class Label {
    private String value;
    private String language;

    public Label(String value, String language) {
        this.value = value;
        this.language = language;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
