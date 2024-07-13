package com.example.inqooltennisreservationapi.repository;

import com.example.inqooltennisreservationapi.model.entity.CourtSurfaceEntity;

import java.util.List;
import java.util.Optional;

public interface CourtSurfaceRepository extends RepositoryForSoftDeletableEntity {

    Optional<CourtSurfaceEntity> createCourtSurface(CourtSurfaceEntity courtSurfaceEntity);

    Optional<CourtSurfaceEntity> updateCourtSurface(long id, CourtSurfaceEntity updatedCourtSurfaceEntity);

    Optional<CourtSurfaceEntity> deleteCourtSurface(long id);

    Optional<CourtSurfaceEntity> getCourtSurfaceById(long id);

    List<CourtSurfaceEntity> listCourtSurfaces();
}
