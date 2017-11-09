package fr.insalyon.pld.semanticweb.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiSourcedLink extends HashMap<URI.Database, URI> implements Map<URI.Database, URI> {

  public void addSource(URI.Database from, URI uri) {
    put(from, uri);
  }

  public URI linkFromDatabase(URI.Database database) {
    return get(database);
  }

  public static MultiSourcedLink from(List<URI> uriList) {

    MultiSourcedLink multiSourcedLink = new MultiSourcedLink();
    uriList.forEach(uri -> {
      multiSourcedLink.put(uri.database, uri);
    });
    return multiSourcedLink;
  }

}
