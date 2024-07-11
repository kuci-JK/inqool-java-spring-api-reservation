package com.example.inqooltennisreservationapi.service;

import com.example.inqooltennisreservationapi.model.api.CourtSurfaceDTOs;

import java.util.List;

public interface CourSurfaceService {

    CourtSurfaceDTOs.CourtSurfaceResponseDTO getSurface(long id);

    CourtSurfaceDTOs.CourtSurfaceResponseDTO createSurface(CourtSurfaceDTOs.CourtSurfaceModifyParams surface);

    CourtSurfaceDTOs.CourtSurfaceResponseDTO editSurface(long id, CourtSurfaceDTOs.CourtSurfaceModifyParams surface);

    CourtSurfaceDTOs.CourtSurfaceResponseDTO deleteSurface(long id);

    List<CourtSurfaceDTOs.CourtSurfaceResponseDTO> getAllSurfaces();

}
