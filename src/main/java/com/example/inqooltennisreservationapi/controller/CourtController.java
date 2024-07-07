package com.example.inqooltennisreservationapi.controller;

import com.example.inqooltennisreservationapi.model.Court;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

@RestController
@RequestMapping("/courts")
public class CourtController {

    // TODO

    @GetMapping("/")
    public String listCourts() {
        return "TODO list courts";
    }

    @PostMapping("/")
    public String createCourt(@RequestBody Court court) {
        return "TODO create court: " + court;
    }

    @GetMapping("/{courtId}")
    public String getCourt(@PathVariable Long courtId) {
        return "TODO getCourt: " + courtId;
    }

    @PostMapping("/{courtId}")
    public String updateCourt(@PathVariable Long courtId) {
        return "TODO updateCourt: " + courtId;
    }

    @DeleteMapping("/{courtId}")
    public String deleteCourt(@PathVariable Long courtId) {
        return "TODO deleteCourt: " + courtId;
    }

}
