package com.example.inqooltennisreservationapi.repository.impl;

import com.example.inqooltennisreservationapi.model.entity.ReservationEntity;
import com.example.inqooltennisreservationapi.repository.ReservationRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.Predicate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationRepositoryImpl implements ReservationRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<ReservationEntity> createReservation(ReservationEntity reservationEntity) {
        entityManager.persist(reservationEntity);
        return Optional.of(reservationEntity);
    }

    @Override
    public Optional<ReservationEntity> updateReservation(long id, ReservationEntity updatedReservationEntity) {
        var existingReservation = entityManager.find(ReservationEntity.class, id);
        if (existingReservation == null) {
            return Optional.empty();
        }
        existingReservation.setCreatedDate(updatedReservationEntity.getCreatedDate());
        existingReservation.setReservationStart(updatedReservationEntity.getReservationStart());
        existingReservation.setReservationEnd(updatedReservationEntity.getReservationEnd());
        existingReservation.setGameType(updatedReservationEntity.getGameType());
        existingReservation.setTotalPrice(updatedReservationEntity.getTotalPrice());
        existingReservation.setReservedCourtEntity(updatedReservationEntity.getReservedCourtEntity());
        existingReservation.setUserEntity(updatedReservationEntity.getUserEntity());

        return Optional.ofNullable(entityManager.merge(existingReservation));
    }

    @Override
    public Optional<ReservationEntity> deleteReservation(long id) {
        var existingReservation = entityManager.find(ReservationEntity.class, id);
        if (existingReservation == null) {
            return Optional.empty();
        }
        existingReservation.setDeleted(true);
        return updateReservation(id, existingReservation);
    }

    @Override
    public Optional<ReservationEntity> getReservationById(long id) {
        return Optional.ofNullable(entityManager.find(ReservationEntity.class, id));
    }

    @Override
    public List<ReservationEntity> listReservations(long courId, boolean futureOnly) {
        return listReservations(Optional.of(courId), Optional.empty(), futureOnly);
    }

    @Override
    public List<ReservationEntity> listReservations(String phone, boolean futureOnly) {
        return listReservations(Optional.empty(), Optional.of(phone), futureOnly);
    }

    private List<ReservationEntity> listReservations(Optional<Long> courId, Optional<String> phone, boolean futureOnly) {
        var builder = entityManager.getCriteriaBuilder();
        var query = builder.createQuery(ReservationEntity.class);
        var root = query.from(ReservationEntity.class);

        List<Predicate> predicates = new ArrayList<>();
        courId.ifPresent(aLong -> {
            predicates.add(builder.equal(root.get("court").get("id"), aLong));
            query.orderBy(builder.asc(root.get("createdDate")));
        });
        phone.ifPresent(s -> {
            predicates.add(builder.equal(root.get("phone"), s));
            query.orderBy(builder.asc(root.get("reservationStart")));
        });
        if (futureOnly) {
            var now = LocalDateTime.now();
            builder.greaterThan(root.get("reservationStart"), now);
        }
        query.where(predicates.toArray(new Predicate[0]));

        query.select(root);

        return entityManager.createQuery(query).getResultList();
    }
}
