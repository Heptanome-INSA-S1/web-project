package fr.insalyon.pld.semanticweb.services.sparqldsl;

public interface QueryBuilderWhere extends QueryBuilderLimit {

    QueryBuilderUnion where(Condition condition);
    QueryBuilderUnion where(Condition condition0,Condition condition1);
    QueryBuilderUnion where(Condition condition0,Condition condition1,Condition condition2);
    QueryBuilderUnion where(Condition condition0,Condition condition1,Condition condition2, Condition condition3);
    QueryBuilderUnion where(Condition... conditions);

}
