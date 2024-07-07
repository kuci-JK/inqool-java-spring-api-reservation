package com.example.inqooltennisreservationapi.controller;

import com.example.inqooltennisreservationapi.model.Court;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    // TODO

    @GetMapping
    public String listReservations() {
        return "TODO list reservations\n";
    }

    @PostMapping
    public String createReservation(@RequestBody Court court) {
        return "TODO create reservation: " + court + "\n";
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
