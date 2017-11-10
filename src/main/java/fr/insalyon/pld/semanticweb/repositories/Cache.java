package fr.insalyon.pld.semanticweb.repositories;

import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Cache {

  private static class CustomMap extends HashMap<String, Document> implements Map<String, Document> {

    public CustomMap() {
    }

    private void removeSome() {

      while (size() > 50) {
        String[] keys = (String[]) keySet().toArray();
        int randomIndex = randomGenerator.nextInt(size());
        String randomKey = keys[randomIndex];
        remove(randomKey);
      }

    }

    @Override
    public Document put(String key, Document value) {

      if(size() > 100) {
        removeSome();
      }
      return super.put(key, value);
    }
  }

  private static Random randomGenerator;
  private static Map<String, Document> globalCache = new CustomMap();

  public static Map<String, Document> getGlobalCache() {

    if(globalCache.size() > 100) {

      for(int i = 0 ; i < 10; i++) {
        String[] keys = (String[]) globalCache.keySet().toArray();
        int randomIndex = randomGenerator.nextInt(globalCache.size());
        String randomKey = keys[randomIndex];
      }

    }

    return globalCache;
  }

}
