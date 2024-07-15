package com.example.inqooltennisreservationapi.model.mappers;

import com.example.inqooltennisreservationapi.exceptions.EntityNotFoundException;
import com.example.inqooltennisreservationapi.exceptions.InvalidRequestException;
import com.example.inqooltennisreservationapi.model.api.ReservationDTOs;
import com.example.inqooltennisreservationapi.model.entity.CourtEntity;
import com.example.inqooltennisreservationapi.model.entity.ReservationEntity;
import com.example.inqooltennisreservationapi.model.entity.UserEntity;
import com.example.inqooltennisreservationapi.repository.CourtRepository;
import com.example.inqooltennisreservationapi.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class ReservationMapper {

    private final CourtMapper courtMapper;

    private final CourtRepository courtRepository;

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    @Autowired
    public ReservationMapper(CourtMapper courtMapper, CourtRepository courtRepository, UserMapper userMapper, UserRepository userRepository) {
        this.courtMapper = courtMapper;
        this.courtRepository = courtRepository;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    private static @NotNull ReservationEntity getReservationEntity(ReservationDTOs.@NotNull ReservationModifyParams reservationModifyParams, @NotNull CourtEntity court, @NotNull Optional<UserEntity> user) {
        return new ReservationEntity(
                0,
                LocalDateTime.now(),
                reservationModifyParams.getReservationStart(),
                reservationModifyParams.getReservationEnd(),
                reservationModifyParams.getGameType(),
                court,
                user.orElse(new UserEntity(
                        reservationModifyParams.getCustomer().getName(),
                        reservationModifyParams.getCustomer().getPhone())
                )
        );
    }

    public @NotNull ReservationDTOs.ReservationResponseDTO entityToResponseDto(@NotNull ReservationEntity reservation) {

        return new ReservationDTOs.ReservationResponseDTO(
                reservation.getId(),
                reservation.getCreatedDate(),
                reservation.getReservationStart(),
                reservation.getReservationEnd(),
                reservation.getGameType(),
                reservation.getTotalPrice(),
                courtMapper.entityToResponseDto(reservation.getReservedCourtEntity()),
                userMapper.entityToResponseDto(reservation.getUserEntity()),
                reservation.isDeleted()
        );
    }

    public @NotNull ReservationEntity dtoToEntity(ReservationDTOs.@NotNull ReservationModifyParams reservationModifyParams) {
        var court = courtRepository.getCourtById(reservationModifyParams.getCourtId());
        if (court.isEmpty()) {
            throw new EntityNotFoundException(String.format("Court id: %s not found", reservationModifyParams.getCourtId()));
        }

        var user = userRepository.getUserByPhone(reservationModifyParams.getCustomer().getPhone());
        if (user.isPresent() && !user.get().getName().equals(reservationModifyParams.getCustomer().getName())) {
            throw new InvalidRequestException("Invalid user data");
        }

        return getReservationEntity(reservationModifyParams, court.get(), user);
    }

}
