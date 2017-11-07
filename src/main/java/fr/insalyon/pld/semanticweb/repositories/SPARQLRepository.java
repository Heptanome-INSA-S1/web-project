package fr.insalyon.pld.semanticweb.repositories;

import fr.insalyon.pld.semanticweb.services.sparqldsl.QueryBuilder;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static fr.insalyon.pld.semanticweb.tools.HttpHelper.httpHelper;

@Component
public interface SPARQLRepository<M> {

    String HTTP_DBPEDIA_ORG = "http://dbpedia.org/";
    String HTTP_WWW_LINKEDMDB_ORG = "http://www.linkedmdb.org/";
    String HTTP_DATA_LINKEDMDB_ORG = "http://data.linkedmdb.org/";

    default List<List<String>> execute(QueryBuilder query) {

        List<List<String>> result = new ArrayList<>();
        RDFConnection imdbConnection = RDFConnectionFactory.connect(HTTP_WWW_LINKEDMDB_ORG);
        RDFConnection dbpediaConnection = RDFConnectionFactory.connect(HTTP_DBPEDIA_ORG);

        List<String> resources = Arrays.asList(query.getSelectClause().split(" ")).stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());
        imdbConnection.querySelect(query.buildWithPrefix(),solution -> {
            List<String> row = new ArrayList<>();
            resources.forEach(resourceName -> row.add(solution.getResource(resourceName).getURI()));
            result.add(row);
        });

        dbpediaConnection.querySelect(query.buildWithPrefix(), solution -> {
            List<String> row = new ArrayList<>();
            resources.forEach(resourceName -> row.add(solution.getResource(resourceName).getURI()));
            result.add(row);
        });

        return result;

    }


    default List<List<Supplier<Document>>> fetch(QueryBuilder query) {
        return execute(query)
                .stream()
                .map(list ->
                        list.stream()
                                .map(uri -> (Supplier<Document>) () -> httpHelper(uri).header("Accept", "application/rdf+xml,application/xml").getDocument())
                                .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    default List<List<Supplier<M>>> fetchAndTransform(QueryBuilder query) {
        return fetch(query)
                .stream()
                .map(list ->
                        list.stream()
                                .map(doc -> (Supplier<M>) () -> hydrate(doc.get()))
                                .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    Optional<M> findById(String uri);
    List<M> findAll();
    List<M> findByName(String name);
    M hydrate(Document document);


    default <E> E orNull(Supplier<E> supplier) {

        try {
            return supplier.get();
        } catch (Exception ignored) {
            return null;
        }

    }

    default Element getElementByFilteredTag(Document document, String tag, String attribute, String filter) {
        
        return document
                .getElementsByTag(tag)
                .stream()
                .filter(marker -> marker.attr(attribute).equals(filter)).collect(Collectors.toList()).get(0);
        
    }


}
