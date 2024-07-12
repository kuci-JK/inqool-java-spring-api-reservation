package com.example.inqooltennisreservationapi.model.api;

import com.example.inqooltennisreservationapi.exceptions.InvalidRequestException;
import com.example.inqooltennisreservationapi.model.GameType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ReservationDTOs {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReservationModifyParams {
        @NotNull
        @FutureOrPresent(message = "Reservation cannot start in the past")
        private LocalDateTime reservationStart;

        @NotNull
        @Future(message = "Reservation cannot end in the past")
        private LocalDateTime reservationEnd;

        @NotNull
        private GameType gameType;

        @Positive(message = "courtId must be positive")
        private long courtId;

        @NotNull
        @Valid
        private UserDTOs.UserModifyParams customer;

        public void validateDateInterval() {
            if (reservationStart.isAfter(reservationEnd)) {
                throw new InvalidRequestException("Reservation cannot start after it ends");
            }
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReservationResponseDTO {
        private long id;
        private LocalDateTime created;
        private LocalDateTime reservationStart;
        private LocalDateTime reservationEnd;
        private GameType gameType;
        private CourtDTOs.CourtResponseDTO court;
        private UserDTOs.UserResponseDTO customer;
    }

}
