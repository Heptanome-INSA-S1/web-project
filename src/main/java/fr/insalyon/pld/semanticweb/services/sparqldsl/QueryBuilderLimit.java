package fr.insalyon.pld.semanticweb.services.sparqldsl;

public interface QueryBuilderLimit extends QueryBuilder {

    QueryBuilderLimit limit(Integer limit);
    QueryBuilderLimit offset(Integer offset);
    QueryBuilderLimit orderAsc(String column);
    QueryBuilderLimit orderDesc(String column);

}
