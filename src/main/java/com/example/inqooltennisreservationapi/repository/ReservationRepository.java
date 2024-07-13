package com.example.inqooltennisreservationapi.repository;

import com.example.inqooltennisreservationapi.model.entity.ReservationEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends RepositoryForSoftDeletableEntity {

    Optional<ReservationEntity> createReservation(ReservationEntity reservationEntity);

    Optional<ReservationEntity> updateReservation(long id, ReservationEntity updatedReservationEntity);

    Optional<ReservationEntity> deleteReservation(long id);

    Optional<ReservationEntity> getReservationById(long id);

    List<ReservationEntity> listReservations(long courId, boolean futureOnly);

    List<ReservationEntity> listReservations(String phone, boolean futureOnly);

    List<ReservationEntity> listReservationsByCourt(long courtId);

    boolean overlapsExistingReservations(
            long courtId, LocalDateTime from, LocalDateTime to, Optional<Long> ignoreReservationId
    );

}
