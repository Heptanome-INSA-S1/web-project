package fr.insalyon.pld.semanticweb.tools;

import com.google.common.collect.ImmutableList;
import javafx.util.Pair;

import java.util.*;
import java.util.function.Function;

public class Kotlin {

    public static <R> List<R> mutableListOf() {
        return new ArrayList<>();
    }

    public static <R> List<R> mutableListOf(R[] args) {
        if(args.length == 0) {
            return new ArrayList<>();
        } else {
            return Arrays.asList(args);
        }
    }

    public static <R> ImmutableList<R> listOf() {
        return ImmutableList.of();
    }

    public static <R> ImmutableList<R> listOf(R[] args) {
        return ImmutableList.copyOf(args);
    }

    public static <R> ImmutableList<R> List(Integer size, Function<Integer, R> init) {
        List<R> list = new ArrayList<>(size);
        for(Integer index = 0; index < size; index++) {
            list.add(init.apply(index));
        }
        return ImmutableList.copyOf(list);
    }

    public static <K,V> Map<K,V> mutableMapOf() {
        return new LinkedHashMap<>();
    }

    public static <K,V> Map<K,V> mutableMapOf(Pair<K,V>[] pairs) {
        Map<K,V> result = new LinkedHashMap<>();
        for(int i = 0; i < pairs.length; i++) {
            result.put(pairs[i].getKey(), pairs[i].getValue());
        }
        return result;
    }

}
