package fr.insalyon.pld.semanticweb.repositories;

import fr.insalyon.pld.semanticweb.repositories.entities.utils.CustomResultSet;
import fr.insalyon.pld.semanticweb.repositories.entities.utils.MultiSourcedDocument;
import fr.insalyon.pld.semanticweb.repositories.entities.utils.MultiSourcedLink;
import fr.insalyon.pld.semanticweb.repositories.entities.utils.URI;
import fr.insalyon.pld.semanticweb.services.sparqldsl.QueryBuilder;
import fr.insalyon.pld.semanticweb.util.Lazy;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static fr.insalyon.pld.semanticweb.tools.HttpHelper.httpHelper;


@Component
public interface SPARQLRepository<M> {

  String HTTP_DBPEDIA_ORG = "http://dbpedia.org/";
  String HTTP_WWW_LINKEDMDB_ORG = "http://www.linkedmdb.org/";
  String HTTP_DATA_LINKEDMDB_ORG = "http://data.linkedmdb.org/";

  Optional<M> findById(String uuid);

  List<M> findAll();

  List<M> findByName(String name);

  /**
   * Return a list of rows. Each row contains a list of URI
   *
   * @param query The sparql query to execute
   * @return
   */
  CustomResultSet<MultiSourcedLink> execute(QueryBuilder query);

  /**
   * Return a list of lazy Document
   *
   * @param query
   * @return
   */
  default CustomResultSet<Lazy<MultiSourcedDocument>> fetch(QueryBuilder query) {

    CustomResultSet<MultiSourcedLink> rows = execute(query);
    return rows.map(multiSourcedLink -> {
      MultiSourcedDocument multiSourcedDocument;
      if (multiSourcedLink == null) {
        multiSourcedDocument = null;
      } else {
        multiSourcedDocument = new MultiSourcedDocument();
        multiSourcedLink.forEach((db, uri) -> {
          try {
            if(Cache.getGlobalCache().get(uri.uri) == null) {
              Cache.getGlobalCache().put(uri.uri, httpHelper(uri.uri).header("Accept", "application/rdf+xml, application/xml").getDocument());
            }
            multiSourcedDocument.put(db, Cache.getGlobalCache().get(uri.uri));
          } catch (Exception ignored) {
          }
        });
      }

      return (Lazy<MultiSourcedDocument>) () -> multiSourcedDocument.isEmpty() ? null : multiSourcedDocument;
    });

  }

  default M retrieve(MultiSourcedLink multiSourcedLink) {
    if (multiSourcedLink == null) return null;
    MultiSourcedDocument multiSourcedDocument = new MultiSourcedDocument();
    multiSourcedLink.forEach((db, link) -> {
      if(Cache.getGlobalCache().get(link.uri) == null) {
        Cache.getGlobalCache().put(link.uri, httpHelper(link.uri).header("Accept", "application/rdf+xml, application/xml").getDocument());
      }
      multiSourcedDocument.put(db, Cache.getGlobalCache().get(link.uri));
    });
    return hydrate(multiSourcedDocument);
  }

  default List<M> retrieveFromURI(List<URI> uriList) {
    if (uriList == null) return new ArrayList<>();
    List<M> result = new ArrayList<>();
    uriList.forEach(uri -> {
      try {
        MultiSourcedDocument multiSourcedDocument = new MultiSourcedDocument();
        if (Cache.getGlobalCache().get(uri.uri) == null) {
          Cache.getGlobalCache().put(uri.uri, httpHelper(uri.uri).header("Accept", "application/rdf+xml, application/xml").getDocument());
        }
        multiSourcedDocument.put(uri.database, Cache.getGlobalCache().get(uri.uri));
        result.add(hydrate(multiSourcedDocument));
      } catch (Exception ignored) {
      }

    });
    return result;
  }

  default M retrieveFromURI(URI uri) {
    if (uri == null) return null;
    try {
      MultiSourcedDocument multiSourcedDocument = new MultiSourcedDocument();
      if(Cache.getGlobalCache().get(uri.uri) == null) {
        Cache.getGlobalCache().put(uri.uri, httpHelper(uri.uri).header("Accept", "application/rdf+xml, application/xml").getDocument());
      }
      multiSourcedDocument.put(uri.database, Cache.getGlobalCache().get(uri.uri));
      return hydrate(multiSourcedDocument);
    } catch (Exception ignored) {
      return null;
    }
  }

  /**
   * Return a list of "Light" lazy M
   *
   * @param query
   * @return
   */
  default List<List<Lazy<M>>> fetchAndTransform(QueryBuilder query) {
    return fetch(query).map(multiSourcedDocumentLazy -> (Lazy<M>) () -> {
      if (multiSourcedDocumentLazy.get() == null) return null;
      else return hydrate(multiSourcedDocumentLazy.get());
    }).toListOfList();
  }

  /**
   * Create a light model from the document
   *
   * @param document
   * @return
   */
  M hydrate(MultiSourcedDocument document);

  default <E> E orNull(Supplier<E> supplier) {

    try {
      return supplier.get();
    } catch (Exception ignored) {
      return null;
    }

  }

  default <E> E orNull(Supplier<E>... suppliers) {

    for (int i = 0; i < suppliers.length; i++) {
      try {
        E result = suppliers[i].get();
        if (result != null) return suppliers[i].get();
      } catch (Exception ignored) {
      }
    }
    return null;
  }

  default <E> List<E> orEmpty(Supplier<List<E>> supplier) {
    try {
      List<E> result = supplier.get();
      if (result == null) {
        return new ArrayList<>();
      } else {
        return result;
      }
    } catch (Exception ignored) {
      return new ArrayList<E>();
    }
  }

  default <E> List<E> orEmpty(Supplier<List<E>>... suppliers) {
    List<E> result = new ArrayList<>();
    for (int i = 0; i < suppliers.length; i++) {
      try {
        result.addAll(suppliers[i].get());
      } catch (Exception ignored) {
      }
    }
    return result;
  }

  default Element getElementByFilteredTag(Document document, String tag, String attribute, String filter) {
    return document
        .getElementsByTag(tag)
        .stream()
        .filter(marker -> marker.attr(attribute).equals(filter)).collect(Collectors.toList()).get(0);
  }

  default List<URI> extractResourceFrom(Document document, String tag) {
    return document.getElementsByTag(tag).stream().map(element -> URI.from(element.attr("rdf:resource"))).collect(Collectors.toList());
  }

}
