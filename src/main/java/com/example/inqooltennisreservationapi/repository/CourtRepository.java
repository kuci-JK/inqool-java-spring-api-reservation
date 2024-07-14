package com.example.inqooltennisreservationapi.repository;

import com.example.inqooltennisreservationapi.model.entity.CourtEntity;

import java.util.List;
import java.util.Optional;

public interface CourtRepository extends RepositoryForSoftDeletableEntity {

    Optional<CourtEntity> createCourt(CourtEntity courtEntity);

    Optional<CourtEntity> updateCourt(long id, CourtEntity updatedCourtEntity);

    Optional<CourtEntity> deleteCourt(long id);

    Optional<CourtEntity> getCourtById(long id);

    List<CourtEntity> listCourtsForSurface(long surfaceId);

    List<CourtEntity> listCourts();


}
