package fr.insalyon.pld.semanticweb.tools;

import fr.insalyon.pld.semanticweb.model.output.Annotation;
import fr.insalyon.pld.semanticweb.model.output.SearchLink;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

import static fr.insalyon.pld.semanticweb.extensions.CollectionExt.flatten;
import static java.lang.Integer.valueOf;

public class HttpHelper implements KotlinClass<HttpHelper> {

    public final String urlName;
    private final Map<String, Object> parameters = new HashMap<>();
    private final Map<String, String> headers = new HashMap<>();

    public HttpHelper() {
        this.urlName = "";
    }

    public HttpHelper(String urlName) {
        this.urlName = urlName;
    }

    public static HttpHelper httpHelper(String url) {
        return new HttpHelper(url);
    }

    protected HttpHelper clone() {
        HttpHelper clone = new HttpHelper(urlName);
        clone.parameters.putAll(parameters);
        clone.headers.putAll(headers);
        return clone;
    }

    public String url() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(urlName);
        if(!parameters.isEmpty()) {
            stringBuilder.append("?");
        }
        parameters.forEach( (key, value) -> {
            stringBuilder.append(key);
            stringBuilder.append("=");
            stringBuilder.append(toUrlParameter(Objects.toString(value)));
            stringBuilder.append("&");
        });
        if(!parameters.isEmpty()) {
            stringBuilder.setLength(stringBuilder.length() - "&".length());
        }
        return stringBuilder.toString();
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
            try {
                stringBuilder.append(URLEncoder.encode(Objects.toString(value), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                stringBuilder.append("");
            }
            stringBuilder.append("&");
        });

        if (!parameters.isEmpty()) {
            stringBuilder.setLength(stringBuilder.length() - 1);
        }

        return stringBuilder.toString();

    }

    public HttpHelper queryParam(String parameterName, Object parameterValue) {
        HttpHelper result = clone();
        result.parameters.put(parameterName, parameterValue);
        return result;
    }

    public HttpHelper header(String key, String value) {
        HttpHelper clone = clone();
        clone.headers.put(key, value);
        return clone;
    }

    public Connection.Response getResponse() {
        Connection connection = Jsoup.connect(url());
        for(Map.Entry<String, String> header : headers.entrySet()) {
            connection = connection.header(header.getKey(), header.getValue());
        }
        try {
            return connection.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Document getDocument() {
        try {
            return getResponse().parse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Element getBody() {
        return getDocument().body();
    }

    public List<SearchLink> getLinks() throws IOException, XPathExpressionException {

        Document document = Jsoup.connect(buildUrl()).get();

        return document
                .getElementsByClass("rc")
                .stream()
                .map(node -> {
                    String url = node.getElementsByTag("a").get(0).attr("href");
                    String title = node.getElementsByTag("a").get(0).text();
                    String description = node.getElementsByClass("st").get(0).text();
                    return new SearchLink(url, title, description);
                }).collect(Collectors.toList());
    }

    public List<SearchLink> fillContent(List<SearchLink> links) throws IOException {

        return links
                .stream()
                .map(link -> {

                    try {
                        Document result = Jsoup.connect(link.url).get();

                        if (result.body().getElementsByTag("p").isEmpty()) {
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

    public List<Annotation> getAnnotations() {

        try {
            return HttpConnection.connect(buildUrl())
                    .header("Accept", "application/xml")
                    .get()
                    .getElementsByTag("Resource")
                    .stream()
                    .map(xmlElement -> {
                        String uri = xmlElement.attr("URI");
                        String support = xmlElement.attr("support");
                        String[] types = xmlElement.attr("types").split(",");
                        String surface = xmlElement.attr("surfaceForm");

                        return new Annotation(surface, types, valueOf(support), uri);

                    }).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static String toString(Node node) {
        if (node instanceof TextNode) {
            return ((TextNode) node).text();
        }
        StringBuilder str = new StringBuilder();
        node
                .childNodes()
                .forEach(it -> str.append(toString(it)));
        return str.toString();
    }
    private static String toUrlParameter(String string) {
        try {
            return URLEncoder.encode(string, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
