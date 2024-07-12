package com.example.inqooltennisreservationapi.service.impl;

import com.example.inqooltennisreservationapi.exceptions.DatabaseException;
import com.example.inqooltennisreservationapi.exceptions.EntityNotFoundException;
import com.example.inqooltennisreservationapi.model.api.ReservationDTOs;
import com.example.inqooltennisreservationapi.model.mappers.ReservationMapper;
import com.example.inqooltennisreservationapi.repository.ReservationRepository;
import com.example.inqooltennisreservationapi.service.ReservationService;
import com.example.inqooltennisreservationapi.validation.ReservationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final ReservationValidator validator;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository, ReservationMapper reservationMapper, ReservationValidator reservationValidator) {
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
        this.validator = reservationValidator;
    }

    @Override
    public ReservationDTOs.ReservationResponseDTO getReservation(long id) {
        var entity = reservationRepository.getReservationById(id);
        if (entity.isEmpty()) {
            throw new EntityNotFoundException(String.format("Reservation (id: %s) not found", id));
        }
        return reservationMapper.entityToResponseDto(entity.get());
    }

    @Override
    public ReservationDTOs.ReservationResponseDTO createReservation(ReservationDTOs.ReservationModifyParams reservation) {
        validator.validateReservationRequest(Optional.empty(), reservation);

        var newEntity = reservationMapper.dtoToEntity(reservation);

        var res = reservationRepository.createReservation(newEntity);
        if (res.isEmpty()) {
            throw new DatabaseException("Failed to create reservation");
        }
        return reservationMapper.entityToResponseDto(res.get());
    }

    @Override
    public ReservationDTOs.ReservationResponseDTO editReservation(long id, ReservationDTOs.ReservationModifyParams reservation) {
        if (!reservationExists(id)) {
            throw new EntityNotFoundException(String.format("Reservation (id: %s) not found", id));
        }
        validator.validateReservationRequest(Optional.of(id), reservation);

        var toSave = reservationMapper.dtoToEntity(reservation);
        toSave.setId(id);

        var res = reservationRepository.updateReservation(id, toSave);
        if (res.isEmpty()) {
            throw new DatabaseException("Failed to update reservation");
        }
        return reservationMapper.entityToResponseDto(res.get());
    }

    @Override
    public ReservationDTOs.ReservationResponseDTO deleteReservation(long id) {
        if (!reservationExists(id)) {
            throw new EntityNotFoundException(String.format("Reservation (id: %s) not found", id));
        }

        var res = reservationRepository.deleteReservation(id);
        if (res.isEmpty()) {
            throw new DatabaseException("Failed to delete reservation");
        }
        return reservationMapper.entityToResponseDto(res.get());
    }

    @Override
    public List<ReservationDTOs.ReservationResponseDTO> getReservationsOnCourt(long courtId, boolean futureOnly) {
        return reservationRepository.listReservations(courtId, futureOnly).stream().map(reservationMapper::entityToResponseDto).toList();
    }

    @Override
    public List<ReservationDTOs.ReservationResponseDTO> getReservationsForUser(String phone, boolean futureOnly) {
        return reservationRepository.listReservations(phone, futureOnly).stream().map(reservationMapper::entityToResponseDto).toList();
    }

    private boolean reservationExists(long id) {
        return reservationRepository.getReservationById(id).isPresent();
    }
}
