package fr.insalyon.pld.semanticweb.entities;

import fr.insalyon.pld.semanticweb.repositories.SPARQLRepository;

public class URI {

    enum Database {
        DBPEDIA("DBPedia", SPARQLRepository.HTTP_DBPEDIA_ORG),
        IMDB("IMDb", SPARQLRepository.HTTP_DATA_LINKEDMDB_ORG);

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

    public static URI from(String uri) {

        if(uri.startsWith(Database.DBPEDIA.url)) {
            return new URI(uri.substring(Database.DBPEDIA.url.length(), uri.length()), Database.DBPEDIA, uri);
        } else if(uri.startsWith(Database.IMDB.url)) {
            return new URI(uri.substring(Database.IMDB.url.length(), uri.length()), Database.IMDB, uri);
        }

        return null;

    }


}
