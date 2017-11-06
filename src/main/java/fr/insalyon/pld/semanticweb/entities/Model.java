package fr.insalyon.pld.semanticweb.entities;

import com.google.gson.Gson;

public interface Model {

    default String toJson() {

        Gson gson = new Gson();
        return gson.toJson(this);

    }

}
