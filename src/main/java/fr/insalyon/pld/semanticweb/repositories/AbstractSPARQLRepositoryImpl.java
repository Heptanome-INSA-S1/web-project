package fr.insalyon.pld.semanticweb.repositories;

import fr.insalyon.pld.semanticweb.entities.CustomResultSet;
import fr.insalyon.pld.semanticweb.entities.MultiSourcedLink;
import fr.insalyon.pld.semanticweb.entities.URI;
import fr.insalyon.pld.semanticweb.services.sparqldsl.QueryBuilder;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdfconnection.RDFConnectionFactory;

import java.util.*;

import static fr.insalyon.pld.semanticweb.tools.Kotlin.mutableListOf;

abstract public class AbstractSPARQLRepositoryImpl<E> implements SPARQLRepository<E> {

  private String simplify(String str) {
    return str.replaceAll("(\\W?)", "").replaceAll(" ", "").toLowerCase();
  }

  @Override
  public CustomResultSet<MultiSourcedLink> execute(QueryBuilder query) {
    return transform(load(query));
  }

  private List<Map<String, MultiSourcedLink>> load(QueryBuilder query) {

    System.out.println(query.buildWithPrefix());

    ResultSet dbpediaResult = RDFConnectionFactory.connect(HTTP_DBPEDIA_ORG).query(query.buildWithPrefix()).execSelect();
    ResultSet linkedmdbResult = RDFConnectionFactory.connect("http://data.linkedmdb.org/").query(query.buildWithPrefix()).execSelect();

    List<String> resources = Arrays.asList(query.getSelectClause().split(" "));
    List<Map<String, MultiSourcedLink>> internal = new ArrayList<>();
    resources.forEach(unused -> internal.add(new HashMap<>()));
    join(dbpediaResult, URI.Database.DBPEDIA, internal, resources);
    join(linkedmdbResult, URI.Database.LINKED_MDB, internal, resources);

    return internal;
  }
  private void join(ResultSet resultSet, URI.Database from, List<Map<String, MultiSourcedLink>> result, List<String> resources) {

    resultSet.forEachRemaining(querySolution -> {
      for (int col = 0; col < resources.size(); col++) {
        String resourceKey = "?joinlabel" + resources.get(col).substring(1, resources.get(col).length());
        String name = querySolution.getLiteral(resourceKey).getString();
        String resourceURI = querySolution.getResource(resources.get(col)).getURI();

        Map<String, MultiSourcedLink> map = result.get(col);

        String uuid = simplify(name);
        if (map.get(uuid) == null) {
          map.put(uuid, new MultiSourcedLink());
        }
        map.get(uuid).put(from, URI.from(resourceURI));
      }
    });

  }
  private CustomResultSet<MultiSourcedLink> transform(List<Map<String, MultiSourcedLink>> internalResult) {

    int rowCount = 0;
    for (int col = 0; col < internalResult.size(); col++) {
      if (rowCount < internalResult.get(col).size()) {
        rowCount = internalResult.get(col).size();
      }
    }

    CustomResultSet<MultiSourcedLink> result = CustomResultSet.init(rowCount, internalResult.size());

    for (int col = 0; col < internalResult.size(); col++) {
      List<MultiSourcedLink> valuesAsList = mutableListOf(internalResult.get(col).values());
      for (int row = 0; row < valuesAsList.size(); row++) {
        result.get(row).set(col, valuesAsList.get(row));
      }
    }

    return result;

  }




}
