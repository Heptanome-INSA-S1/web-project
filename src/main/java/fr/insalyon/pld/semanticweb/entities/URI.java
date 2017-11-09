package fr.insalyon.pld.semanticweb.entities;

import fr.insalyon.pld.semanticweb.repositories.SPARQLRepository;

public class URI {

    public enum Database {
        DBPEDIA("DBPedia", SPARQLRepository.HTTP_DBPEDIA_ORG),
        LINKED_MDB("Linkedmdb", SPARQLRepository.HTTP_DATA_LINKEDMDB_ORG),
        Unknown("unknown", "");

        public final String name;
        public final String url;

        Database(String name, String url) {
            this.name = name;
            this.url = url;
        }

        @Override
        public String toString() {
            return name;
        }
    };

    public final String anchor;
    public final Database database;
    public final String uri;

    public URI(String anchor, Database database, String uri) {
        this.anchor = anchor;
        this.database = database;
        this.uri = uri;
    }

    public static URI from(String string) {

        Database db;
        if(string.startsWith(Database.DBPEDIA.url)) {
            db = Database.DBPEDIA;
        } else if(string.startsWith(Database.LINKED_MDB.url)) {
            db = Database.LINKED_MDB;
        } else {
            db = Database.Unknown;
        }

        String anchor = string.substring(db.url.length(), string.length());
        return new URI(anchor, db, string);

    }

}
