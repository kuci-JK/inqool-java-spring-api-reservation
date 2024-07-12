package com.example.inqooltennisreservationapi.controller;

import com.example.inqooltennisreservationapi.model.api.CourtSurfaceDTOs;
import com.example.inqooltennisreservationapi.service.CourtSurfaceService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/surfaces")
@Validated
public class SurfaceController {

    private final CourtSurfaceService service;

    @Autowired
    public SurfaceController(CourtSurfaceService service) {
        this.service = service;
    }

    @GetMapping
    public List<CourtSurfaceDTOs.CourtSurfaceResponseDTO> listSurfaces() {
        return service.getAllSurfaces();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CourtSurfaceDTOs.CourtSurfaceResponseDTO createSurface(@RequestBody @Valid CourtSurfaceDTOs.CourtSurfaceModifyParams params) {
        return service.createSurface(params);
    }

    @GetMapping("{surfaceId}")
    public CourtSurfaceDTOs.CourtSurfaceResponseDTO getSurface(@PathVariable @Positive long surfaceId) {
        return service.getSurface(surfaceId);
    }

    @PutMapping("{surfaceId}")
    public CourtSurfaceDTOs.CourtSurfaceResponseDTO updateSurface(@PathVariable @Positive long surfaceId, @RequestBody @Valid CourtSurfaceDTOs.CourtSurfaceModifyParams params) {
        return service.editSurface(surfaceId, params);
    }

    @DeleteMapping("{surfaceId}")
    public CourtSurfaceDTOs.CourtSurfaceResponseDTO deleteSurface(@PathVariable @Positive long surfaceId) {
        return service.deleteSurface(surfaceId);
    }

}
