package com.example.inqooltennisreservationapi.repository.impl;

import com.example.inqooltennisreservationapi.exceptions.DatabaseException;
import com.example.inqooltennisreservationapi.model.entity.CourtSurfaceEntity;
import com.example.inqooltennisreservationapi.repository.CourtRepository;
import com.example.inqooltennisreservationapi.repository.CourtSurfaceRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CourtSurfaceRepositoryImpl implements CourtSurfaceRepository {

    private final CourtRepository courtRepo;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public CourtSurfaceRepositoryImpl(CourtRepository courtRepo) {
        this.courtRepo = courtRepo;
    }

    @Override
    @Transactional
    public Optional<CourtSurfaceEntity> createCourtSurface(CourtSurfaceEntity courtSurfaceEntity) {
        entityManager.persist(courtSurfaceEntity);
        return Optional.of(entityManager.find(CourtSurfaceEntity.class, courtSurfaceEntity.getId()));
    }

    @Override
    @Transactional
    public Optional<CourtSurfaceEntity> updateCourtSurface(long id, CourtSurfaceEntity updatedCourtSurfaceEntity) {
        CourtSurfaceEntity existingCourtSurfaceEntity = entityManager.find(CourtSurfaceEntity.class, id);
        if (existingCourtSurfaceEntity == null || existingCourtSurfaceEntity.isDeleted()) {
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
        if (courtSurfaceEntity == null || courtSurfaceEntity.isDeleted()) {
            return Optional.empty();
        }

        if (!courtRepo.listCourtsBySurface(id).isEmpty()) {
            throw new DatabaseException("Cannot delete court surface that is in use");
        }

        courtSurfaceEntity.setDeleted(true);
        return updateCourtSurface(id, courtSurfaceEntity);
    }

    @Override
    public Optional<CourtSurfaceEntity> getCourtSurfaceById(long id) {
        if (!entityExists(entityManager, CourtSurfaceEntity.class, id)) {
            return Optional.empty();
        }
        return Optional.ofNullable(entityManager.find(CourtSurfaceEntity.class, id));
    }

    @Override
    public List<CourtSurfaceEntity> listCourtSurfaces() {

        var builder = entityManager.getCriteriaBuilder();
        var query = builder.createQuery(CourtSurfaceEntity.class);
        var root = query.from(CourtSurfaceEntity.class);

        query.where(
                entityNotDeletetPredicate(builder, root)
        );
        query.orderBy(builder.asc(root.get("id")));

        query.select(root);
        return entityManager.createQuery(query).getResultList();
    }
}
