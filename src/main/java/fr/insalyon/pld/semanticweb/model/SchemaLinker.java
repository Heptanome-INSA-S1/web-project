package fr.insalyon.pld.semanticweb.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class SchemaLinker {
    private Map<String,String> namespace = new HashMap<String, String>();

    public SchemaLinker(){
        namespace.put("dbo", "http://dbpedia.org/ontology/");
        namespace.put("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
        namespace.put("foaf", "http://xmlns.com/foaf/0.1/");
        namespace.put("owl", "http://www.w3.org/2002/07/owl#");
        namespace.put("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
    }

    public String namespace(String prefix){
        return namespace.get(prefix);
    }

    public static class Actor{
        public List<String> typePredicates = Arrays.asList("dbo:Actor");
        public List<String> namePredicates = Arrays.asList("dbo:birthName","foaf:name","rdfs:label");
        public List<String> playInPredicates = Arrays.asList("dbo:movie");
    }
}
