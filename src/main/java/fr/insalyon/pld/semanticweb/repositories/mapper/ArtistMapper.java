package fr.insalyon.pld.semanticweb.repositories.mapper;

import fr.insalyon.pld.semanticweb.entities.Artist;
import fr.insalyon.pld.semanticweb.model.persistence.ArtistModel;

import java.util.ArrayList;

public class ArtistMapper implements Mapper<Artist, ArtistModel> {

    @Override
    public ArtistModel entityToLightModel(Artist entity) {

        return new ArtistModel(
                entity.name,
                entity.firstName,
                entity.birthDate,
                entity.deathDate,
                entity.biography,
                new ArrayList<>(),
                null,
                new ArrayList<>(),
                new ArrayList<>()
        );

    }

    @Override
    public ArtistModel entityToFullModel(Artist entity) {
        return null;
    }
}
