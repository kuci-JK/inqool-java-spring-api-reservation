package com.example.inqooltennisreservationapi.validation;

import com.example.inqooltennisreservationapi.exceptions.InvalidRequestException;
import com.example.inqooltennisreservationapi.model.api.ReservationDTOs;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface ReservationValidator {
    void validateReservationRequest(
            Optional<Long> reservationId,
            @NotNull ReservationDTOs.ReservationModifyParams data
    ) throws InvalidRequestException;
}
