package fr.insalyon.pld.semanticweb.model.persistence;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchemaLinker {

    public static String IS = "rdf:type";

    public Map<String, String> namespace = new HashMap<>();
    private static SchemaLinker instance = null;

    public static SchemaLinker get() {
        if(instance == null) {
            instance = new SchemaLinker();
        }
        return instance;
    }

    private SchemaLinker() {
        namespace.put("dbo",      "http://dbpedia.org/ontology/");
        namespace.put("rdf",      "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
        namespace.put("rdfs",     "http://www.w3.org/2000/01/rdf-schema#");
        namespace.put("schema",   "http://schema.org/");
        namespace.put("foaf",     "http://xmlns.com/foaf/0.1/");
        namespace.put("yago",     "http://yago-knowledge.org/resource/");
        namespace.put("umbel-rc", "http://umbel.org/umbel/rc/");
        namespace.put("dbp",      "http://dbpedia.org/property/");
        namespace.put("movie",    "http://data.linkedmdb.org/resource/movie/");
    }

    public String namespace(String prefix) {
        return namespace.get(prefix);
    }

    public static class Actor {

        public static List<String> type = Arrays.asList("yago:YagoLegalActor", "umbel-rc:Actor", "yago:Actor109765278", "yago:WikicatActors");
        public static List<String> hasName = Arrays.asList("foaf:name", "rdfs:label");
        public static String hasBiography = "dbo:abstract";

    }

    public static class Movie {

        public static String hasSynopsis = "dbo:abstract";
        public static String thumbnail = "dbo:thumbnail";
        public static List<String> type = Arrays.asList("dbo:Movie", "schema:Movie");
        public static List<String> hasName = Arrays.asList("foaf:name", "rdfs:label");
        public static String hasActor = "dbo:starring";

    }

    public static class TVShow {

        public static class Season {
            public static String type = "dbo:TelevisionSeason";
            public static String hasNumberOfEpisodes = "dbo:numberOfEpisodes";
            public static String showName = "dbp:showName";

            public static class Episode {
                public static List<String> type = Arrays.asList("dbo:TelevisionEpisode", "schema:TVEpisode");
                public static String isEpisodeOf = "dbp:episodeList";
            }
        }

        public static String hasGenre = "dbo:genre";
        public static String hasSynopsis = "dbo:abstract";
        public static String thumbnail = "dbo:thumbnail";
        public static String type = "umbel-rc:TVShow_IBT";
        public static List<String> hasName = Arrays.asList("foaf:name", "rdfs:label");
        public static String hasActor = "dbo:starring";
        public static String hasNumberOfSeason = "dbo:numberOfSeasons";

    }

}
