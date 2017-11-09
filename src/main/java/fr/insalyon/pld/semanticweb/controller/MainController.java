package fr.insalyon.pld.semanticweb.controller;


import fr.insalyon.pld.semanticweb.model.output.Annotation;
import fr.insalyon.pld.semanticweb.model.output.DBpediaQuery;
import fr.insalyon.pld.semanticweb.model.output.JsonObject;
import fr.insalyon.pld.semanticweb.model.output.SearchLink;
import fr.insalyon.pld.semanticweb.model.persistence.SchemaLinker;
import fr.insalyon.pld.semanticweb.model.persistence.SchemaLinker.TVShow;
import fr.insalyon.pld.semanticweb.repositories.entities.utils.MultiSourcedLink;
import fr.insalyon.pld.semanticweb.repositories.entities.utils.URI;
import fr.insalyon.pld.semanticweb.repositories.services.MovieRepository;
import fr.insalyon.pld.semanticweb.tools.HttpHelper;
import javafx.util.Pair;
import org.apache.commons.io.FileUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static fr.insalyon.pld.semanticweb.extensions.StringExt.splitOfLength;
import static fr.insalyon.pld.semanticweb.extensions.StringExt.toUrlParameter;
import static fr.insalyon.pld.semanticweb.model.output.JsonObject.jsonObjectOf;
import static fr.insalyon.pld.semanticweb.model.persistence.SchemaLinker.IS;
import static fr.insalyon.pld.semanticweb.model.tuple.Triplet.tripletOf;
import static fr.insalyon.pld.semanticweb.services.sparqldsl.Filter.like;
import static fr.insalyon.pld.semanticweb.services.sparqldsl.QueryBuilderImpl.select;
import static fr.insalyon.pld.semanticweb.tools.HttpHelper.httpHelper;
import static fr.insalyon.pld.semanticweb.tools.Kotlin.mutableListOf;
import static fr.insalyon.pld.semanticweb.tools.Kotlin.mutableMapOf;

@CrossOrigin
@Controller
public class MainController {

  MovieRepository movieRepository = new MovieRepository();

  private String renderHtml(String view) throws IOException {
    return FileUtils.readFileToString(new File("src/main/resources/" + view + ".html"), "UTF-8");
  }

  @RequestMapping("/")
  public @ResponseBody
  String index() throws IOException {
    return renderHtml("templates/index");
  }

  @RequestMapping("/searchLinks")
  public @ResponseBody
  List<SearchLink> queryGoogle(
      @RequestParam(value = "query", defaultValue = "") String query,
      @RequestParam(value = "offset", defaultValue = "0") String offset
  ) throws IOException, XPathExpressionException {

    final HttpHelper httpHelper = new HttpHelper("https://www.google.fr/search")
        .queryParam("q", query)
        .queryParam("start", offset);
    if (query.isEmpty()) {
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
  Map<String, List<Annotation>> analyse(
      @RequestBody DBpediaQuery dBpediaQuery
  ) {

    Map<String, List<Annotation>> response = mutableMapOf();

    dBpediaQuery.resources.forEach(searchLink -> {

      response.put(searchLink.url, mutableListOf());
      splitOfLength(searchLink.content, 2048).forEach(subcontent -> {

        HttpHelper httpHelper = new HttpHelper("http://model.dbpedia-spotlight.org/fr/annotate")
            .queryParam("text", toUrlParameter(subcontent))
            .queryParam("confidence", dBpediaQuery.confidence)
            .queryParam("support", dBpediaQuery.support);

        ((List<Annotation>) response.get(searchLink.url)).addAll(httpHelper.getAnnotations());
      });

    });

    return response;

  }

  @RequestMapping("/getRdf")
  public @ResponseBody
  JsonObject getRdf(
      @RequestBody SearchLink searchLink
  ) throws IOException {
    return toCleanJson(ModelFactory.createDefaultModel().read(searchLink.url));
  }

  @RequestMapping("/autocomplete")
  @ResponseBody
  Object autocomplete() {

    String query = select("?serie")
        .where(
            tripletOf("?serie", IS, TVShow.type),
            tripletOf("?serie", TVShow.hasName, "?showName"),
            like("?showName", "^game of")
        ).limit(5).build();

    return httpHelper("http://dbpedia.org/sparql")
        .queryParam("default-graph-uri", "http://dbpedia.org")
        .queryParam("query", query)
        .queryParam("timeout", 1000)
        .queryParam("format", "application/rdf+xml")
        .queryParam("debug", "off")
        .also(it -> System.out.println(it.url()))
        .getDocument().getElementsByTag("res:value").stream().map(it -> it.attr("rdf:resource")).collect(Collectors.toList());
  }

  @RequestMapping(value = "/test", produces = "application/json")
  @ResponseBody
  Object test(
      @RequestParam(name = "uuid") String uuid
  ) {

    MovieRepository movieRepository = new MovieRepository();
    return movieRepository.findById(uuid).get();

  }

  private JsonObject toCleanJson(Model self) {

    JsonObject jsonObject = jsonObjectOf();

    self.listSubjects().forEachRemaining(subject -> {

      List<JsonObject> properties = new ArrayList<>();
      subject.listProperties().forEachRemaining(triplet -> {
        String predicateName = triplet.getPredicate().getNameSpace() + triplet.getPredicate().getLocalName();
        try {
          if (!triplet.getLanguage().isEmpty()) {
            predicateName += "[@" + triplet.getLanguage() + "]";
          }
        } catch (Exception ignored) {
        }
        properties.add(
            jsonObjectOf(new Pair<>(predicateName, triplet.getObject().toString()))
        );
      });

      jsonObject.put(subject.toString(), properties);
    });

    return jsonObject;


  }

  private String toHtml(String self) {
    return self
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll("\n", "<br/>")
        .replaceAll(" ", "&nbsp;");
  }

}
