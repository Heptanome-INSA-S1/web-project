package fr.insalyon.pld.semanticweb.tools;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class Util {

    public static <R> List<R> mutableListOf() {
        return new ArrayList<>();
    }

    public static <R> List<R> mutableListOf(R... args) {
        if(args.length == 0) {
            return new ArrayList<>();
        } else {
            return Arrays.asList(args);
        }
    }

    public static <R> ImmutableList<R> ListOf() {
        return ImmutableList.of();
    }

    public static <R> ImmutableList<R> listOf(R... args) {
        return ImmutableList.copyOf(args);
    }

    public static <R> ImmutableList<R> List(Integer size, Function<Integer, R> init) {
        List<R> list = new ArrayList<>(size);
        for(Integer index = 0; index < size; index++) {
            list.add(init.apply(index));
        }
        return ImmutableList.copyOf(list);
    }

}
