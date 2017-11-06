package fr.insalyon.pld.semanticweb.repositories;

import fr.insalyon.pld.semanticweb.services.sparqldsl.QueryBuilderWhere;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static fr.insalyon.pld.semanticweb.tools.HttpHelper.httpHelper;

@Component
public interface SPARQLRepository<M> {

    String defaultResourcePath = "http://dbpedia.org/resource/";

    default List<List<String>> execute(String query) {

        List<List<String>> rows = new ArrayList<>();
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
                });
        return rows;

    }

    default List<List<Supplier<Document>>> fetch(String query) {
        return execute(query)
                .stream()
                .map(list ->
                        list.stream()
                                .map(uri -> (Supplier<Document>) () -> httpHelper(uri).header("Accept", "application/rdf+xml").getDocument())
                                .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    default List<List<Supplier<M>>> fetchAndTransform(String query) {
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
