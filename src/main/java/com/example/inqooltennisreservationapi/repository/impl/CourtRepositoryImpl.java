package com.example.inqooltennisreservationapi.repository.impl;

import com.example.inqooltennisreservationapi.exceptions.DatabaseException;
import com.example.inqooltennisreservationapi.model.entity.CourtEntity;
import com.example.inqooltennisreservationapi.model.entity.CourtSurfaceEntity;
import com.example.inqooltennisreservationapi.repository.CourtRepository;
import com.example.inqooltennisreservationapi.repository.ReservationRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class CourtRepositoryImpl implements CourtRepository {

    private final ReservationRepository reservationRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public CourtRepositoryImpl(ReservationRepository reservationRepo) {
        this.reservationRepo = reservationRepo;
    }

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

        var now = LocalDateTime.now();
        var reservations = reservationRepo.listReservationsByCourt(id);
        if (reservations.stream().anyMatch(r -> now.isBefore(r.getReservationEnd()))) {
            throw new DatabaseException("Cannot delete court. Court has non finished reservations");
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
    public List<CourtEntity> listCourtsBySurface(long surfaceId) {
        var builder = entityManager.getCriteriaBuilder();
        var query = builder.createQuery(CourtEntity.class);
        var root = query.from(CourtEntity.class);
        query.select(root)
                .where(builder.and(
                        entityNotDeletetPredicate(builder, root),
                        builder.equal(root.get("surface").<CourtSurfaceEntity>get("id"), surfaceId)
                ));

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<CourtEntity> listCourts() {
        return entityManager.createQuery(
                "from CourtEntity where deleted = false order by name asc", CourtEntity.class
        ).getResultList();
    }
}
