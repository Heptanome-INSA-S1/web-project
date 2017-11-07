package fr.insalyon.pld.semanticweb.services.sparqldsl;

public interface QueryBuilder {

    String getSelectClause();
    String build();
    String buildWithPrefix();

}

