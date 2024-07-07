package com.example.inqooltennisreservationapi.controller;

import com.example.inqooltennisreservationapi.model.CourtSurface;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/surfaces")
public class SurfaceController {

    @GetMapping("/")
    public String listSurfaces() {
        // TODO list all surfaces
        return "TODO list surfaces";
    }

    @PostMapping("/")
    public String createSurface(@RequestBody CourtSurface surface) {
        return "TODO create surface: " + surface;
    }

    @GetMapping("/{surfaceId}" )
    public String getSurface(@PathVariable Long surfaceId) {
        return "TODO get surface " + surfaceId;
    }

    @PutMapping("/{surfaceId}" )
    public String updateSurface(@PathVariable Long surfaceId, @RequestBody CourtSurface data) {
        return "TODO update surface: '" + surfaceId + "' - data: " + data;
    }

    @DeleteMapping("/{surfaceId}")
    public String deleteSurface(@PathVariable Long surfaceId) {
        return "TODO delete surface: '" + surfaceId + "'";
    }


}
