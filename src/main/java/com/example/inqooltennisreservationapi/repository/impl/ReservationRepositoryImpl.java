package com.example.inqooltennisreservationapi.repository.impl;

import com.example.inqooltennisreservationapi.model.entity.CourtEntity;
import com.example.inqooltennisreservationapi.model.entity.ReservationEntity;
import com.example.inqooltennisreservationapi.model.entity.UserEntity;
import com.example.inqooltennisreservationapi.repository.ReservationRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
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
    @Transactional
    public Optional<ReservationEntity> createReservation(ReservationEntity reservationEntity) {
        entityManager.persist(reservationEntity);
        return Optional.of(entityManager.find(ReservationEntity.class, reservationEntity.getId()));
    }

    @Override
    @Transactional
    public Optional<ReservationEntity> updateReservation(long id, ReservationEntity updatedReservationEntity) {
        var existingReservation = entityManager.find(ReservationEntity.class, id);
        if (existingReservation == null || existingReservation.isDeleted()) {
            return Optional.empty();
        }
        existingReservation.setCreatedDate(updatedReservationEntity.getCreatedDate());
        existingReservation.setReservationStart(updatedReservationEntity.getReservationStart());
        existingReservation.setReservationEnd(updatedReservationEntity.getReservationEnd());
        existingReservation.setGameType(updatedReservationEntity.getGameType());
        existingReservation.setReservedCourtEntity(updatedReservationEntity.getReservedCourtEntity());
        existingReservation.setUserEntity(updatedReservationEntity.getUserEntity());

        return Optional.ofNullable(entityManager.merge(existingReservation));
    }

    @Override
    @Transactional
    public Optional<ReservationEntity> deleteReservation(long id) {
        var existingReservation = entityManager.find(ReservationEntity.class, id);
        if (existingReservation == null || existingReservation.isDeleted()) {
            return Optional.empty();
        }
        existingReservation.setDeleted(true);
        return updateReservation(id, existingReservation);
    }

    @Override
    @Transactional
    public Optional<ReservationEntity> getReservationById(long id) {
        if (!entityExists(entityManager, ReservationEntity.class, id)) {
            return Optional.empty();
        }
        return Optional.ofNullable(entityManager.find(ReservationEntity.class, id));
    }

    @Override
    @Transactional
    public List<ReservationEntity> listReservations(long courId, boolean futureOnly) {
        return listReservations(Optional.of(courId), Optional.empty(), futureOnly);
    }

    @Override
    @Transactional
    public List<ReservationEntity> listReservations(String phone, boolean futureOnly) {
        return listReservations(Optional.empty(), Optional.of(phone), futureOnly);
    }

    @Override
    @Transactional
    public boolean overlapsExistingReservations(
            long courtId, LocalDateTime from, LocalDateTime to, Optional<Long> ignoreReservationId
    ) {
        var builder = entityManager.getCriteriaBuilder();
        var query = builder.createQuery(ReservationEntity.class);
        var root = query.from(ReservationEntity.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(entityNotDeletetPredicate(builder, root));

        predicates.add(builder.equal(root.get("reservedCourtEntity").<CourtEntity>get("id"), courtId));
        ignoreReservationId.ifPresent(id -> predicates.add(builder.notEqual(root.get("id"), id)));

        predicates.add(builder.lessThan(root.get("reservationStart"), to));
        predicates.add(builder.greaterThan(root.get("reservationEnd"), from));

        query.where(predicates.toArray(new Predicate[0]));
        query.select(root);

        return !entityManager.createQuery(query).getResultList().isEmpty();
    }

    @Transactional
    private List<ReservationEntity> listReservations(Optional<Long> courtId, Optional<String> phone, boolean futureOnly) {
        var builder = entityManager.getCriteriaBuilder();
        var query = builder.createQuery(ReservationEntity.class);
        var root = query.from(ReservationEntity.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(entityNotDeletetPredicate(builder, root));

        courtId.ifPresent(aLong -> {
            predicates.add(builder.equal(root.get("reservedCourtEntity").get("id"), aLong));
            query.orderBy(builder.asc(root.get("createdDate")));
        });
        phone.ifPresent(s -> {
            predicates.add(builder.equal(root.get("userEntity").<UserEntity>get("phone"), builder.parameter(String.class, "phone")));
            query.orderBy(builder.asc(root.get("reservationStart")));
        });
        if (futureOnly) {
            var now = LocalDateTime.now();
            builder.greaterThan(root.get("reservationStart"), now);
        }
        query.where(predicates.toArray(new Predicate[0]));

        query.select(root);

        var actualQuery = entityManager.createQuery(query);
        phone.ifPresent(s -> actualQuery.setParameter("phone", s));
        return actualQuery.getResultList();
    }

    @Override
    @Transactional
    public List<ReservationEntity> listReservationsByCourt(long courtId) {
        return entityManager.createQuery("from ReservationEntity where reservedCourtEntity.id = :courtId", ReservationEntity.class)
                .setParameter("courtId", courtId)
                .getResultList();
    }
}
