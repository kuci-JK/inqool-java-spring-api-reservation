package com.example.inqooltennisreservationapi.controller;

import com.example.inqooltennisreservationapi.model.entity.CourtEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courts")
public class CourtController {

    // TODO

    @GetMapping
    public String listCourts() {
        return "TODO list courts\n";
    }

    @PostMapping
    public String createCourt(@RequestBody CourtEntity courtEntity) {
        return "TODO create court: " + courtEntity + "\n";
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
