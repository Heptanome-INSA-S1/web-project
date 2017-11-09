package fr.insalyon.pld.semanticweb.tools;

import java.util.function.Consumer;
import java.util.function.Function;

public interface KotlinClass<K> {

    default K unwrap() {
        return (K) this;
    }

    default <U> U let(Function<K, U> block) {
        return block.apply(this.unwrap());
    }

    default K also(Consumer<K> block) {
        block.accept(this.unwrap());
        return this.unwrap();
    }

    default K apply(Function<K, K> block) {
        return block.apply(this.unwrap());
    }

}
