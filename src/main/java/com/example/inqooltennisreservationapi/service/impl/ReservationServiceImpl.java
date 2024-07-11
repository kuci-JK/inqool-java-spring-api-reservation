package com.example.inqooltennisreservationapi.service.impl;

import com.example.inqooltennisreservationapi.model.api.ReservationDTOs;
import com.example.inqooltennisreservationapi.model.mappers.ReservationMapper;
import com.example.inqooltennisreservationapi.repository.ReservationRepository;
import com.example.inqooltennisreservationapi.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository, ReservationMapper reservationMapper) {
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
    }

    @Override
    public ReservationDTOs.ReservationResponseDTO getReservation(long id) {
        var entity = reservationRepository.getReservationById(id);
        if (entity.isEmpty()) {
            throw new RuntimeException("Entity not found"); // TODO
        }
        return reservationMapper.entityToResponseDto(entity.get());
    }

    @Override
    public ReservationDTOs.ReservationResponseDTO createReservation(ReservationDTOs.ReservationModifyParams reservation) {
        var newEntity = reservationMapper.dtoToEntity(reservation);
        if (newEntity == null) {
            throw new RuntimeException("Entity not found"); // TODO
        }
        var res = reservationRepository.createReservation(newEntity);
        if (res.isEmpty()) {
            throw new RuntimeException("Creation of reservation failed");
        }
        return reservationMapper.entityToResponseDto(res.get());
    }

    @Override
    public ReservationDTOs.ReservationResponseDTO editReservation(long id, ReservationDTOs.ReservationModifyParams reservation) {
        var toSave = reservationMapper.dtoToEntity(reservation);
        toSave.setId(id);

        var res = reservationRepository.updateReservation(id, toSave);
        if (res.isEmpty()) {
            throw new RuntimeException("Updating reservation failed"); // TODO
        }
        return reservationMapper.entityToResponseDto(res.get());
    }

    @Override
    public ReservationDTOs.ReservationResponseDTO deleteReservation(long id) {
        var res = reservationRepository.deleteReservation(id);
        if (res.isEmpty()) {
            throw new RuntimeException("Deleting reservation failed");
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
}
