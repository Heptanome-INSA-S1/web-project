package fr.insalyon.pld.semanticweb.model;

public class Annotation {

    public final String surface;
    public final String[] types;
    public final Integer support;
    public final String uri;

    public Annotation(String surface, String[] types, Integer support, String uri) {
        this.surface = surface;
        this.types = types;
        this.support = support;
        this.uri = uri;
    }
}
