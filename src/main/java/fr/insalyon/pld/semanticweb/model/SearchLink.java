package fr.insalyon.pld.semanticweb.model;

public class SearchLink {

    public final String url;
    public final String title;
    public final String description;
    public final String content;

    public SearchLink() {
        this(null, null, null, null);
    }

    public SearchLink(String url) {
        this(url, null, null, null);
    }
    public SearchLink(String url, String title) {
        this(url, title, null, null);
    }
    public SearchLink(String url, String title, String description) {
        this(url, title, description, null);
    }
    public SearchLink(String url, String title, String description, String content) {
        this.url = url;
        this.title = title;
        this.description = description;
        this.content = content;
    }

    public SearchLink withContent(String content) {
        return new SearchLink(url, title, description, content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchLink searchLink = (SearchLink) o;

        return url != null ? url.equals(searchLink.url) : searchLink.url == null;
    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }
}
