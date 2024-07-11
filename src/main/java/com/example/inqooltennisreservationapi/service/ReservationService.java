package com.example.inqooltennisreservationapi.service;

import com.example.inqooltennisreservationapi.model.api.ReservationDTOs;

import java.util.List;

public interface ReservationService {

    ReservationDTOs.ReservationResponseDTO getReservation(long id);

    ReservationDTOs.ReservationResponseDTO createReservation(ReservationDTOs.ReservationModifyParams reservation);

    ReservationDTOs.ReservationResponseDTO editReservation(long id, ReservationDTOs.ReservationModifyParams reservation);

    ReservationDTOs.ReservationResponseDTO deleteReservation(long id);

    List<ReservationDTOs.ReservationResponseDTO> getReservationsOnCourt(long courtId, boolean futureOnly);

    List<ReservationDTOs.ReservationResponseDTO> getReservationsForUser(String phone, boolean futureOnly);

}
