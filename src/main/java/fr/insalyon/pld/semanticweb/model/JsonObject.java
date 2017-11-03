package fr.insalyon.pld.semanticweb.model;

import javafx.util.Pair;

import java.util.LinkedHashMap;
import java.util.Map;

import static fr.insalyon.pld.semanticweb.tools.Kotlin.mutableMapOf;

public class JsonObject extends LinkedHashMap<Object, Object> implements Map<Object, Object> {

    private JsonObject() {
        super();
    }

    public static JsonObject jsonObjectOf() {
        return new JsonObject();
    }

    public static JsonObject jsonObjectOf(Pair<Object, Object> pair) {
        JsonObject result = new JsonObject();
        result.put(pair.getKey(), pair.getValue());
        return result;
    }

    public static JsonObject jsonObjectOf(Pair<Object, Object>[] pairs) {
        JsonObject result = new JsonObject();
        result.putAll(mutableMapOf(pairs));
        return result;
    }

}
