package fr.insalyon.pld.semanticweb.services.sparqldsl;

import static fr.insalyon.pld.semanticweb.services.sparqldsl.QueryBuilderImpl.Keywords.FILTER;

public interface Filter extends Condition {

    static Filter like(String subject, String regex) {
        return like(subject, regex, false);
    }

    static Filter like(String subject, String regex, Boolean caseSensitive) {
        if(caseSensitive) {
            return () -> FILTER + " regex(" + subject + ", \"" + regex + "\")";
        } else {
            return () -> FILTER + " regex(" + subject + ", \"" + regex + "\", \"i\")";
        }
    }

    static Filter lang(String subject, String lang) {
        return () -> FILTER + "(lang(" + subject + ")= \"" + lang + "\")";
    }

    static Filter same(String subject, String complement) {
        return () -> FILTER + "(str(" + subject + ") = str(" + complement + "))";
    }

    static Filter hasUri(String subject, String uri) {
        return () -> FILTER + "(str(" + subject + ")=\"" + uri + "\")";
    }

}
