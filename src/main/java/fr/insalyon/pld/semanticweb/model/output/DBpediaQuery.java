package fr.insalyon.pld.semanticweb.model.output;

import java.util.List;

import static fr.insalyon.pld.semanticweb.tools.Kotlin.listOf;

public class DBpediaQuery {

    public final Double confidence;
    public final Integer support;
    public final List<SearchLink> resources;

    public DBpediaQuery() {
        confidence = .5;
        support = 0;
        resources = listOf();
    }

    public DBpediaQuery(Double confidence, Integer support, List<SearchLink> resources) {
        this.confidence = confidence;
        this.support = support;
        this.resources = resources;
    }
}
