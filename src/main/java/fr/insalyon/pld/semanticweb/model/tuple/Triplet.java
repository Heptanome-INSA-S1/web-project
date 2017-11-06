package fr.insalyon.pld.semanticweb.model.tuple;

import fr.insalyon.pld.semanticweb.services.sparqldsl.Condition;
import fr.insalyon.pld.semanticweb.services.sparqldsl.QueryBuilderImpl.Keywords;

import java.util.List;

public class Triplet<A,B,C> implements Condition {

    public final A first;
    public final B second;
    public final C third;

    public Triplet(A first, B second, C third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public static <A, B, C> Triplet tripletOf(A first, B second, C third) {
        return new Triplet<>(first, second, third);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Triplet<?, ?, ?> triplet = (Triplet<?, ?, ?>) o;

        if (first != null ? !first.equals(triplet.first) : triplet.first != null) return false;
        if (second != null ? !second.equals(triplet.second) : triplet.second != null) return false;
        return third != null ? third.equals(triplet.third) : triplet.third == null;
    }

    @Override
    public int hashCode() {
        int result = first != null ? first.hashCode() : 0;
        result = 31 * result + (second != null ? second.hashCode() : 0);
        result = 31 * result + (third != null ? third.hashCode() : 0);
        return result;
    }

    @Override
    public String toSparqlCondition() {

        if(!(first instanceof String)) {
            throw new RuntimeException("Cannot use a non string variable as subject to build a sparql condition");
        }

        if(second instanceof String && third instanceof String) {
            return buildWhereClause((String) first, (String)second, (String) third);
        } else if(second instanceof List<?> && third instanceof String) {
            return buildWhereClause((String) first, (List<String>)second, (String) third);
        } else if(second instanceof String && third instanceof List<?>) {
            return buildWhereClause((String) first, (String)second, (List<String>) third);
        } else if(second instanceof List<?> && third instanceof List<?>) {
            return buildWhereClause((String) first, (List<String>)second, (List<String>) third);
        } else {
            throw new RuntimeException("Unavailable type to transform this triplet into sparql condition");
        }

    }

    private String builParam(String input) {
        if(input.startsWith("http")) {
            return "<" + input + ">";
        }
        return input;
    }

    private String buildWhereClause(String subject, String predicate, String object) {
        if("is".equals(predicate)) {
            return "{" + subject + " rdf:type " + object + "}.";
        } else {
            return "{" + subject + " " + predicate + " " + object + "}.";
        }

    }

    private String buildWhereClause(String subject, List<String> predicates, String object) {

        StringBuilder s = new StringBuilder();

        predicates.forEach(predicate -> {
            s.append("{ ");
            s.append(buildWhereClause(builParam(subject), builParam(predicate), builParam(object)));
            s.append(" }");
            s.append(Keywords.UNION);
        });

        if(!predicates.isEmpty()){
            s.setLength( s.length() - Keywords.UNION.length() );
        }

        s.append(".");

        return s.toString();
    }

    private String buildWhereClause(String subject, String predicate, List<String> objects) {
        StringBuilder s = new StringBuilder();

        objects.forEach(object -> {
            s.append("{ ");
            s.append(buildWhereClause(builParam(subject), builParam(predicate), builParam(object)));
            s.append(" }");
            s.append(Keywords.UNION);
        });

        if (!objects.isEmpty()) {
            s.setLength(s.length() - Keywords.UNION.length());
        }

        s.append(".");

        return s.toString();
    }

    private String buildWhereClause(String subject, List<String> predicates, List<String> objects) {
        StringBuilder s = new StringBuilder();

        predicates.forEach(predicate -> objects.forEach(object -> {
            s.append("{ ");
            s.append(buildWhereClause(builParam(subject), builParam(predicate), builParam(object)));
            s.append(" }");
            s.append(Keywords.UNION);
        }));
        if (!predicates.isEmpty()) {
            s.setLength(s.length() - Keywords.UNION.length());
        }
        s.append(".");

        return s.toString();
    }

}
