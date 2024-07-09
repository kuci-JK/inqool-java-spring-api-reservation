package com.example.inqooltennisreservationapi.controller;

import com.example.inqooltennisreservationapi.model.entity.CourtEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    // TODO

    @GetMapping
    public String listReservations(
            @RequestParam(name = "courtId", required = false) Optional<Long> courtId,
            @RequestParam(name = "phone", required = false) Optional<String> phone,
            @RequestParam(name = "onlyFuture", defaultValue = "false") boolean onlyFuture
    ) {
        if (courtId.isEmpty() && phone.isEmpty()) {
            throw new RuntimeException("Need to provide either courtId or phone number"); // TODO error handling
        }
        return String.format("TODO list reservations sorted by date created: courtId %s, phone %s, onlyFuture %s\n", courtId, phone, onlyFuture);
    }

    @PostMapping
    public String createReservation(@RequestBody CourtEntity courtEntity) {
        return "TODO create reservation: " + courtEntity + "\n";
    }

    @GetMapping("{reservationId}")
    public String getReservation(@PathVariable Long reservationId) {
        return "TODO getReservation: " + reservationId + "\n";
    }

    @PutMapping("{reservationId}")
    public String updateReservation(@PathVariable Long reservationId) {
        return "TODO updateReservation: " + reservationId + "\n";
    }

    @DeleteMapping("{reservationId}")
    public String deleteReservation(@PathVariable Long reservationId) {
        return "TODO deleteReservation: " + reservationId + "\n";
    }
}
