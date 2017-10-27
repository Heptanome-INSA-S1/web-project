package fr.insalyon.pld.semanticweb.tools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

        if(!parameters.isEmpty()) {
            stringBuilder.append("?");
        }

        parameters.forEach((key, value) -> {
            stringBuilder.append(key);
            stringBuilder.append("=");
            String noSpaceValue = value.toString().replaceAll(" ", "+");
            stringBuilder.append(noSpaceValue);
        });

        return stringBuilder.toString();

    }

    public URLRequester with(String parameterName, Object parameterValue) {

        URLRequester result = new URLRequester(urlName);
        parameters.forEach((key, value) -> result.parameters.put(key, value));
        result.parameters.put(parameterName, parameterValue);
        return result;

    }

    public List<String> retrieveUrls() throws IOException, XPathExpressionException {

        Document document = Jsoup.connect(buildUrl()).get();

        Elements titles = document.getElementsByClass("r");

        List<String> links = new ArrayList<>();

        for(Element title : titles) {
            links.addAll(

                    title.getElementsByTag("a").stream().map(
                            link -> link.attr("href")
                                    .replaceAll("%2F","/")
                                    .replaceAll("%3F", ":")
                    ).collect(Collectors.toList())

            );
        }

        links.stream().filter(link -> link.startsWith("http"));

        return links;

    }

    public String outSource() throws IOException {

        Element body = Jsoup.connect(urlName).get().body();

        StringBuilder stringBuilder = new StringBuilder();
        for(Element paragraph : body.getElementsByTag("p")) {

            paragraph.childNodes()
                .stream().filter(node -> node instanceof TextNode)
                .map(node -> ((TextNode)node).text())
                .collect(Collectors.toList())
                .forEach(stringBuilder::append);

        }

        return stringBuilder.toString();

    }

}
