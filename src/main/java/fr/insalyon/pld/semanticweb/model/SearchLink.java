package fr.insalyon.pld.semanticweb.model;

public class Resource {

    public final String url;
    public final String title;
    public final String description;

    public final String content;

    public Resource() {
        this(null, null, null, null);
    }

    public Resource(String url) {
        this(url, null, null, null);
    }
    public Resource(String url, String title) {
        this(url, title, null, null);
    }
    public Resource(String url, String title, String description) {
        this(url, title, description, null);
    }

    private Resource(String url, String title, String description, String content) {
        this.url = url;
        this.title = title;
        this.description = description;
        this.content = content;
    }

    public Resource withContent(String content) {
        return new Resource(url, title, description, content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resource resource = (Resource) o;

        return url != null ? url.equals(resource.url) : resource.url == null;
    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }
}
