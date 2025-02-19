package com.example.inqooltennisreservationapi.model.api;

import com.example.inqooltennisreservationapi.model.GameType;
import com.fasterxml.jackson.annotation.JsonFormat;
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
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private LocalDateTime reservationEnd;

        @NotNull
        private GameType gameType;

        @Positive(message = "courtId must be positive")
        private long courtId;

        @NotNull
        @Valid
        private UserDTOs.UserModifyParams customer;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReservationResponseDTO {
        private long id;

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private LocalDateTime created;

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private LocalDateTime reservationStart;

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private LocalDateTime reservationEnd;

        private GameType gameType;

        private double price;

        private CourtDTOs.CourtResponseDTO court;

        private UserDTOs.UserResponseDTO customer;

        private boolean deleted;
    }

}
