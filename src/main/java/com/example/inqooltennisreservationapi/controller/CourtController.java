package com.example.inqooltennisreservationapi.controller;

import com.example.inqooltennisreservationapi.model.Court;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

@RestController
@RequestMapping("/courts")
public class CourtController {

    // TODO

    @GetMapping
    public String listCourts() {
        return "TODO list courts\n";
    }

    @PostMapping
    public String createCourt(@RequestBody Court court) {
        return "TODO create court: " + court + "\n";
    }

    @GetMapping("{courtId}")
    public String getCourt(@PathVariable Long courtId) {
        return "TODO getCourt: " + courtId + "\n";
    }

    @PutMapping("{courtId}")
    public String updateCourt(@PathVariable Long courtId) {
        return "TODO updateCourt: " + courtId + "\n";
    }

    @DeleteMapping("{courtId}")
    public String deleteCourt(@PathVariable Long courtId) {
        return "TODO deleteCourt: " + courtId + "\n";
    }

}
