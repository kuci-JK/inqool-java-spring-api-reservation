package com.example.inqooltennisreservationapi.controller;

import com.example.inqooltennisreservationapi.exceptions.InvalidRequestException;
import com.example.inqooltennisreservationapi.model.api.ReservationDTOs;
import com.example.inqooltennisreservationapi.service.ReservationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reservations")
@Validated
public class ReservationController {

    private final ReservationService service;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.service = reservationService;
    }

    @GetMapping
    public List<ReservationDTOs.ReservationResponseDTO> listReservations(
            @RequestParam(name = "courtId", required = false) Optional<Long> courtId,
            @RequestParam(name = "phone", required = false) Optional<String> phone,
            @RequestParam(name = "onlyFuture", defaultValue = "false") boolean onlyFuture
    ) {
        if ((courtId.isEmpty() && phone.isEmpty()) || (courtId.isPresent() && phone.isPresent())) {
            throw new InvalidRequestException("Need to provide either courtId or phone number");
        }
        if (courtId.isPresent()) {
            if (courtId.get() <= 0) {
                throw new InvalidRequestException("Invalid courtId");
            }
            return service.getReservationsOnCourt(courtId.get(), onlyFuture);
        }
        if (phone.get().isEmpty()) {
            throw new InvalidRequestException("Phone number must be present");
        }
        return service.getReservationsForUser(phone.get(), onlyFuture);
    }

    @GetMapping("{reservationId}")
    public ReservationDTOs.ReservationResponseDTO getReservation(
            @PathVariable @Positive long reservationId
    ) {
        return service.getReservation(reservationId);
    }

    @PostMapping
    public ReservationDTOs.ReservationResponseDTO createReservation(
            @RequestBody @NotNull @Valid ReservationDTOs.ReservationModifyParams params
    ) {
        return service.createReservation(params);
    }

    @PutMapping("{reservationId}")
    public ReservationDTOs.ReservationResponseDTO updateReservation(
            @PathVariable @Positive long reservationId,
            @RequestBody @NotNull @Valid ReservationDTOs.ReservationModifyParams params
    ) {
        return service.editReservation(reservationId, params);
    }

    @DeleteMapping("{reservationId}")
    public ReservationDTOs.ReservationResponseDTO deleteReservation(@PathVariable Long reservationId) {
        return service.deleteReservation(reservationId);
    }
}
