package com.example.inqooltennisreservationapi.controller;

import com.example.inqooltennisreservationapi.model.api.CourtDTOs;
import com.example.inqooltennisreservationapi.service.CourtService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courts")
@Validated
public class CourtController {

    private final CourtService service;

    @Autowired
    public CourtController(CourtService courtService) {
        this.service = courtService;
    }

    @GetMapping
    public List<CourtDTOs.CourtResponseDTO> listCourts() {
        return service.getAllCourts();
    }

    @PostMapping
    public CourtDTOs.CourtResponseDTO createCourt(@RequestBody @Valid CourtDTOs.CourtModifyParams params) {
        return service.createCourt(params);
    }

    @GetMapping("{courtId}")
    public CourtDTOs.CourtResponseDTO getCourt(@PathVariable @Positive long courtId) {
        return service.getCourt(courtId);
    }

    @PutMapping("{courtId}")
    public CourtDTOs.CourtResponseDTO updateCourt(@PathVariable @Positive long courtId, @RequestBody @Valid CourtDTOs.CourtModifyParams params) {
        return service.editCourt(courtId, params);
    }

    @DeleteMapping("{courtId}")
    public CourtDTOs.CourtResponseDTO deleteCourt(@PathVariable @Positive long courtId) {
        return service.deleteCourt(courtId);
    }

}
