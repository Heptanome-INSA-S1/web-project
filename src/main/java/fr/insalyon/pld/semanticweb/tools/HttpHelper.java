package fr.insalyon.pld.semanticweb.tools;

import fr.insalyon.pld.semanticweb.model.SearchLink;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class URLRequester {

    public final String urlName;
    private final Map<String, Object> parameters = new HashMap<>();

    public URLRequester(String urlName) {
        this.urlName = urlName;
    }

    private String buildUrl() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(urlName);

        if (!parameters.isEmpty()) {
            stringBuilder.append("?");
        }

        parameters.forEach((key, value) -> {
            stringBuilder.append(key);
            stringBuilder.append("=");
            String noSpaceValue = value.toString().replaceAll(" ", "+");
            stringBuilder.append(noSpaceValue);
            stringBuilder.append("&");
        });

        if (!parameters.isEmpty()) {
            stringBuilder.setLength(stringBuilder.length() - 1);
        }

        return stringBuilder.toString();

    }

    public URLRequester with(String parameterName, Object parameterValue) {

        URLRequester result = new URLRequester(urlName);
        parameters.forEach((key, value) -> result.parameters.put(key, value));
        result.parameters.put(parameterName, parameterValue);
        return result;

    }

    public List<SearchLink> retrieveUrls() throws IOException, XPathExpressionException {

        Document document = Jsoup.connect(buildUrl()).get();

        return document
                .getElementsByClass("rc")
                .stream()
                .map(node -> {
                    String url = node.getElementsByTag("a").get(0).attr("href");
                    String title = node.getElementsByTag("a").get(0).text();
                    String description = node.getElementsByClass("st").get(0).text()
                    return new SearchLink(url, title, description);
                }).collect(Collectors.toList());
    }

    public List<SearchLink> fillContent(List<SearchLink> links) throws IOException {

        links
                .stream()
                .map(link -> {

                    try {
                        Document result = Jsoup.connect(link.url).get();

                        if(result.body().getElementsByTag("p").isEmpty()) {
                            return link;
                        } else {
                            StringBuilder stringBuilder = new StringBuilder();
                            flatten(result
                                    .body()
                                    .getElementsByTag("p")
                                    .stream()
                                    .map(paragraph -> paragraph.childNodes())
                                    .collect(Collectors.toList())
                            ).stream()
                                    .map(node -> toString(node))
                                    .forEach(stringBuilder::append);
                            return new SearchLink(link.url, link.title, link.description, stringBuilder.toString());
                        }

                    } catch (Exception e) {
                        return link;
                    }

                })
                .collect(Collectors.toList());

    }

    public String outSource() throws IOException {

        Element body = Jsoup.connect(urlName).get().body();

        StringBuilder stringBuilder = new StringBuilder();
        for (Element paragraph : body.getElementsByTag("p")) {

            paragraph.childNodes()
                    .stream().filter(node -> node instanceof TextNode)
                    .map(node -> ((TextNode) node).text())
                    .collect(Collectors.toList())
                    .forEach(stringBuilder::append);

        }

        return stringBuilder.toString();

    }

    public String getRdf() {
        return "";
    }

    private static <R> List<R> flatten(final Iterable<Iterable<R>> deepList) {
        List<R> result = new ArrayList<R>();
        for(Iterable<R> elements : deepList) {
            addAll(result, elements);
        }
        return result;
    }

    private static <R> Boolean addAll(List<R> list, Iterable<R> elements) {
        if(elements instanceof Collection) {
            return list.addAll((Collection) elements);
        } else {
            Boolean hasAdd = false;
            for(R item : elements) {
                if(list.add(item)) {
                    hasAdd = true;
                }
            }
            return hasAdd;
        }
    }

    private static String toString(Node node) {
        if(node instanceof TextNode) {
            return ((TextNode) node).text();
        }
        StringBuilder str = new StringBuilder();
        node
                .childNodes()
                .forEach(it -> str.append(toString(it)));
        return str.toString();
    }

}
