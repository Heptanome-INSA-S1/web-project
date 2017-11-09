package fr.insalyon.pld.semanticweb.extensions;

import com.google.common.collect.ImmutableList;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class CollectionExt {

    public static <R> List<R> flatten(final Iterable<Iterable<R>> deepList) {
        List<R> result = new ArrayList<>();
        for(Iterable<R> elements : deepList) {
            addAll(result, elements);
        }
        return result;
    }

    public static <R> Boolean addAll(List<R> list, Iterable<R> elements) {
        if(elements instanceof Collection) {
            return list.addAll((Collection) elements);
        } else {
            Boolean hasAdd = false;
            for(R item : elements) {
                if(list.add(item)) {
                    hasAdd = true;
                }
            }
            return hasAdd;
        }
    }

    public static <R> R find(Collection<R> list, Predicate<R> predicate) {
        for(R element : list) {
            if(predicate.test(element)) {
                return element;
            }
        }
        return null;
    }

    public static <R> ImmutableList<ImmutableList<R>> deepImmutableCopyOf(Collection<? extends Collection<R>> deepCollection) {

        List<ImmutableList<R>> partialImmutable = deepCollection.stream().map(ImmutableList::copyOf).collect(Collectors.toList());
        return ImmutableList.copyOf(partialImmutable);

    }

    public static <R> Set<R> toSet(Collection<R> collection) {
        return new LinkedHashSet<>(collection);
    }

    public static <R> Set<R> toSet(Collection<R> collection, Comparator<R> comparator) {
        Set<R> set = new TreeSet<>(comparator);
        set.addAll(collection);
        return set;
    }


}
