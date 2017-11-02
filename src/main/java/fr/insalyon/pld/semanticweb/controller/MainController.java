package fr.insalyon.pld.semanticweb.controller;


import fr.insalyon.pld.semanticweb.extensions.StringExt;
import fr.insalyon.pld.semanticweb.model.Annotation;
import fr.insalyon.pld.semanticweb.model.DBpediaQuery;
import fr.insalyon.pld.semanticweb.model.JsonObject;
import fr.insalyon.pld.semanticweb.model.SearchLink;
import fr.insalyon.pld.semanticweb.tools.HttpHelper;
import fr.insalyon.pld.semanticweb.tools.Kotlin;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static fr.insalyon.pld.semanticweb.extensions.StringExt.splitOfLength;
import static fr.insalyon.pld.semanticweb.extensions.StringExt.toUrlParameter;
import static fr.insalyon.pld.semanticweb.model.JsonObject.jsonObjectOf;
import static fr.insalyon.pld.semanticweb.tools.Kotlin.mutableListOf;

@Controller
public class TestController {

    @RequestMapping("/searchLinks")
    public @ResponseBody
    List<SearchLink> queryGoogle(
            @RequestParam(value = "query", defaultValue = "") String query,
            @RequestParam(value = "offset", defaultValue = "0") String offset
    ) throws IOException, XPathExpressionException {

        final HttpHelper httpHelper = new HttpHelper("https://www.google.fr/search")
                .with("q", query)
                .with("start", offset);
        if(query.isEmpty()) {
            return new ArrayList<>();
        } else {
            return httpHelper.getLinks();
        }
    }

    @RequestMapping(value = "/fillContent", method = RequestMethod.PUT)
    public @ResponseBody
    List<SearchLink> fillContent(
            @RequestBody List<SearchLink> searchLinks
    ) throws IOException {
        return (new HttpHelper()).fillContent(searchLinks);
    }

    @RequestMapping(value = "/analyse", method = RequestMethod.POST)
    public @ResponseBody
    JsonObject analyse(
            @RequestBody DBpediaQuery dBpediaQuery
            ) {

        JsonObject response = jsonObjectOf();

        dBpediaQuery.resources.forEach(searchLink -> {

            response.put(searchLink.url, Kotlin.<Annotation>mutableListOf());
            splitOfLength(searchLink.content, 1024).forEach( subcontent -> {

                HttpHelper httpHelper = new HttpHelper("http://model.dbpedia-spotlight.org/en/annotate")
                        .with("text", toUrlParameter(subcontent))
                        .with("confidence", dBpediaQuery.confidence)
                        .with("support", dBpediaQuery.support);

                ((List<Annotation>) response.get(searchLink.url)).addAll(httpHelper.getAnnotations());


            });

        });

        return response;

    }

    @RequestMapping("/getRdf")
    public @ResponseBody
    String getRdf(
            @RequestBody SearchLink searchLink
    ) throws IOException {
        return (new HttpHelper(searchLink.url)).getRdf().toString();
    }

}
