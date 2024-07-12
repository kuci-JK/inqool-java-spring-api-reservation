package com.example.inqooltennisreservationapi.repository.impl;

import com.example.inqooltennisreservationapi.model.entity.CourtEntity;
import com.example.inqooltennisreservationapi.repository.CourtRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CourtRepositoryImpl implements CourtRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<CourtEntity> createCourt(CourtEntity courtEntity) {
        entityManager.persist(courtEntity);
        return Optional.of(courtEntity);
    }

    @Override
    public Optional<CourtEntity> updateCourt(long id, CourtEntity updatedCourtEntity) {
        var existing = entityManager.find(CourtEntity.class, id);
        if (existing == null) {
            return Optional.empty();
        }
        existing.setName(updatedCourtEntity.getName());
        existing.setSurface(updatedCourtEntity.getSurface());
        existing.setDeleted(updatedCourtEntity.isDeleted());
        var res = entityManager.merge(existing);
        return Optional.of(res);
    }

    @Override
    public Optional<CourtEntity> deleteCourt(long id) {
        var existing = entityManager.find(CourtEntity.class, id);
        if (existing == null) {
            return Optional.empty();
        }
        existing.setDeleted(true);
        return updateCourt(id, existing);
    }

    @Override
    public Optional<CourtEntity> getCourtById(long id) {
        return Optional.ofNullable(entityManager.find(CourtEntity.class, id));
    }

    @Override
    public List<CourtEntity> listCourts() {
        // TODO change ordering ?
        // TODO exclude deleted
        return entityManager.createQuery("from CourtEntity order by name asc", CourtEntity.class).getResultList();
    }
}
