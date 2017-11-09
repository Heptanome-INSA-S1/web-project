package fr.insalyon.pld.semanticweb.tools;

public class IntGenerator {

    private Integer current = 0;
    private Integer step = 1;

    public IntGenerator(Integer step) {
        this.step = step;
    }

    public Integer current() {
        return current;
    }

    public void next() {
        current += step;
    }
}
