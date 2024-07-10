package com.example.inqooltennisreservationapi.model.api;

import com.example.inqooltennisreservationapi.model.GameType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

public class ReservationDTOs {

    @Data
    @AllArgsConstructor
    public static class ReservationModifyParams {
        private LocalDateTime reservationStart;
        private LocalDateTime reservationEnd;
        private GameType gameType;
        private long courtId;
        private UserDTOs.UserModifyParams customer;
    }

    @Data
    @AllArgsConstructor
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
