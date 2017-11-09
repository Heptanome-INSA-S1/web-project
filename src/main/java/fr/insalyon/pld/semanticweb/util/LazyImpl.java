package fr.insalyon.pld.semanticweb.util;

import java.util.function.Supplier;

public class LazyImpl<E> implements Lazy<E> {

  private boolean wasLoaded = false;
  private E result = null;
  private final Supplier<E> getMethod;

  public LazyImpl(Supplier<E> getMethod) {
    this.getMethod = getMethod;
  }

  @Override
  public E get() {
    if(!wasLoaded) {
      result = getMethod.get();
      wasLoaded = true;
    }
    return result;
  }
}
