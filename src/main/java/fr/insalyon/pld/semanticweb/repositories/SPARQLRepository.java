package fr.insalyon.pld.semanticweb.repositories;

import fr.insalyon.pld.semanticweb.services.sparqldsl.QueryBuilder;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.jsoup.nodes.Document;
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

    String defaultResourcePath = "http://dbpedia.org/resource/";

    default List<List<String>> execute(QueryBuilder query) {

        List<List<String>> result = new ArrayList<>();
        RDFConnection conn = RDFConnectionFactory.connect("http://www.linkedmdb.org/");


        List<String> resources = Arrays.asList(query.getSelectClause().split(" ")).stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());
        conn.querySelect(query.buildWithPrefix(),solution -> {
            List<String> row = new ArrayList<>();
            resources.forEach(resourceName -> row.add(solution.getResource(resourceName).getURI()));
            result.add(row);
        });

        /*List<List<String>> rows = new ArrayList<>();
        httpHelper("http://dbpedia.org/sparql")
                .queryParam("default-graph-uri", "http://dbpedia.org")
                .queryParam("query", query)
                .queryParam("timeout", 1000)
                .queryParam("format", "application/rdf+xml")
                .queryParam("debug", "off")
                .also(it -> System.out.println(it.url()))
                .getDocument().getElementsByTag("res:solution").forEach(solution -> {
                    List<String> row = new ArrayList<>();
                    solution.getElementsByTag("res:value").forEach(value -> {
                        row.add(value.attr("rdf:resource"));
                    });
                    rows.add(row);
                });*/
        return result;

    }

    default List<List<Supplier<Document>>> fetch(QueryBuilder query) {
        return execute(query)
                .stream()
                .map(list ->
                        list.stream()
                                .map(uri -> (Supplier<Document>) () -> httpHelper(uri).header("Accept", "application/rdf+xml").getDocument())
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

}
