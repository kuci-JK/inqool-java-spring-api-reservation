package com.example.inqooltennisreservationapi.controller;

import com.example.inqooltennisreservationapi.model.entity.CourtSurfaceEntity;
import com.example.inqooltennisreservationapi.repository.CourtSurfaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/surfaces")
public class SurfaceController {

    CourtSurfaceRepository repo;

    @Autowired
    public SurfaceController(CourtSurfaceRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public String listSurfaces() {
        return repo.listCourtSurfaces().stream().map(CourtSurfaceEntity::toString).reduce("[ ", (String a, String b) -> a + "; " + b) + " ]";
    }

    @PostMapping
    public CourtSurfaceEntity createSurface(@RequestBody CourtSurfaceEntity surface) {
        surface.setId(0);
        var a = repo.createCourtSurface(surface);
        return a.orElse(null);
    }

    @GetMapping("{surfaceId}" )
    public String getSurface(@PathVariable Long surfaceId) {
        return repo.getCourtSurfaceById(surfaceId).toString();
    }

    @PutMapping("{surfaceId}" )
    public String updateSurface(@PathVariable Long surfaceId, @RequestBody CourtSurfaceEntity data) {
        return "TODO update surface: '" + surfaceId + "' - data: " + data + "\n";
    }

    @DeleteMapping("{surfaceId}")
    public String deleteSurface(@PathVariable Long surfaceId) {
        return "TODO delete surface: '" + surfaceId + "'\n";
    }


}
