package fr.insalyon.pld.semanticweb.repositories.mapper;

public interface Mapper<E, M> {

    M entityToLightModel(E entity);
    M entityToFullModel(E entity);

}
