package com.example.inqooltennisreservationapi.repository.impl;

import com.example.inqooltennisreservationapi.model.entity.CourtSurfaceEntity;
import com.example.inqooltennisreservationapi.repository.CourtSurfaceRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CourtSurfaceRepositoryImpl implements CourtSurfaceRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Optional<CourtSurfaceEntity> createCourtSurface(CourtSurfaceEntity courtSurfaceEntity) {
        entityManager.persist(courtSurfaceEntity);
        return Optional.of(courtSurfaceEntity);
    }

    @Override
    @Transactional
    public Optional<CourtSurfaceEntity> updateCourtSurface(long id, CourtSurfaceEntity updatedCourtSurfaceEntity) {
        CourtSurfaceEntity existingCourtSurfaceEntity = entityManager.find(CourtSurfaceEntity.class, id);
        if (existingCourtSurfaceEntity == null) {
            return Optional.empty();
        }

        existingCourtSurfaceEntity.setSurfaceName(updatedCourtSurfaceEntity.getSurfaceName());
        existingCourtSurfaceEntity.setPricePerMinute(updatedCourtSurfaceEntity.getPricePerMinute());
        entityManager.merge(existingCourtSurfaceEntity);
        return Optional.of(existingCourtSurfaceEntity);
    }

    @Override
    @Transactional
    public Optional<CourtSurfaceEntity> deleteCourtSurface(long id) {
        CourtSurfaceEntity courtSurfaceEntity = entityManager.find(CourtSurfaceEntity.class, id);
        if (courtSurfaceEntity == null) {
            return Optional.empty();
        }
        // TODO also remove used ??
        courtSurfaceEntity.setDeleted(true);
        return updateCourtSurface(id, courtSurfaceEntity);
    }

    @Override
    public Optional<CourtSurfaceEntity> getCourtSurfaceById(long id) {
        return Optional.ofNullable(entityManager.find(CourtSurfaceEntity.class, id));
    }

    @Override
    public List<CourtSurfaceEntity> listCourtSurfaces() {
        // TODO decide order...
        // TODO exclude deleted
        return entityManager.createQuery("from CourtSurfaceEntity", CourtSurfaceEntity.class).getResultList();
    }
}
