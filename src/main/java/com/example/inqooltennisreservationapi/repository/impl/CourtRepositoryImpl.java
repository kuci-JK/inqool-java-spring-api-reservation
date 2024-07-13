package com.example.inqooltennisreservationapi.repository.impl;

import com.example.inqooltennisreservationapi.model.entity.CourtEntity;
import com.example.inqooltennisreservationapi.repository.CourtRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CourtRepositoryImpl implements CourtRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Optional<CourtEntity> createCourt(CourtEntity courtEntity) {
        entityManager.persist(courtEntity);
        return Optional.of(courtEntity);
    }

    @Override
    @Transactional
    public Optional<CourtEntity> updateCourt(long id, CourtEntity updatedCourtEntity) {
        var existing = entityManager.find(CourtEntity.class, id);
        if (existing == null || existing.isDeleted()) {
            return Optional.empty();
        }
        existing.setName(updatedCourtEntity.getName());
        existing.setSurface(updatedCourtEntity.getSurface());
        existing.setDeleted(updatedCourtEntity.isDeleted());
        var res = entityManager.merge(existing);
        return Optional.of(res);
    }

    @Override
    @Transactional
    public Optional<CourtEntity> deleteCourt(long id) {
        var existing = entityManager.find(CourtEntity.class, id);
        if (existing == null || existing.isDeleted()) {
            return Optional.empty();
        }
        existing.setDeleted(true);
        return updateCourt(id, existing);
    }

    @Override
    public Optional<CourtEntity> getCourtById(long id) {
        if (!entityExists(entityManager, CourtEntity.class, id)) {
            return Optional.empty();
        }
        return Optional.ofNullable(entityManager.find(CourtEntity.class, id));
    }

    @Override
    public List<CourtEntity> listCourts() {
        return entityManager.createQuery(
                "from CourtEntity where deleted = false order by name asc", CourtEntity.class
        ).getResultList();
    }
}
