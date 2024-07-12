package com.example.inqooltennisreservationapi.validation.impl;

import com.example.inqooltennisreservationapi.exceptions.InvalidRequestException;
import com.example.inqooltennisreservationapi.model.api.ReservationDTOs;
import com.example.inqooltennisreservationapi.repository.ReservationRepository;
import com.example.inqooltennisreservationapi.validation.ReservationValidator;
import com.example.inqooltennisreservationapi.validation.UserValidator;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ReservationValidatorImpl implements ReservationValidator {

    private final ReservationRepository repo;
    private final UserValidator userValidator;

    @Autowired
    public ReservationValidatorImpl(ReservationRepository reservationRepository, UserValidator userValidator) {
        this.repo = reservationRepository;
        this.userValidator = userValidator;
    }

    @Override
    public void validateReservationRequest(Optional<Long> reservationId, @NotNull ReservationDTOs.ReservationModifyParams data) throws InvalidRequestException {
        userValidator.validateUserDataMatchOrUserNotExists(data.getCustomer());
        validateDateInterval(data);

        var hasOverlaps = repo.overlapsExistingReservations(
                data.getCourtId(), data.getReservationStart(), data.getReservationEnd(), reservationId
        );
        if (hasOverlaps) {
            throw new InvalidRequestException("Reservation overlaps other reservation");
        }
    }

    private void validateDateInterval(@NotNull ReservationDTOs.ReservationModifyParams data) {
        if (data.getReservationStart().isAfter(data.getReservationEnd())) {
            throw new InvalidRequestException("Reservation cannot start after it ends");
        }
    }
}
