package fr.insalyon.pld.semanticweb.services.sparqldsl;

public interface QueryBuilderUnion extends QueryBuilderLimit {

    QueryBuilderUnion union(Condition condition);
    QueryBuilderUnion union(Condition condition0,Condition condition1);
    QueryBuilderUnion union(Condition condition0,Condition condition1,Condition condition2);
    QueryBuilderUnion union(Condition condition0,Condition condition1,Condition condition2, Condition condition3);
    QueryBuilderUnion union(Condition... conditions);

}
