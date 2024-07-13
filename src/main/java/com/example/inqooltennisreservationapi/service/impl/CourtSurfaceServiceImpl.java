package com.example.inqooltennisreservationapi.service.impl;

import com.example.inqooltennisreservationapi.exceptions.DatabaseException;
import com.example.inqooltennisreservationapi.exceptions.EntityNotFoundException;
import com.example.inqooltennisreservationapi.model.api.CourtSurfaceDTOs;
import com.example.inqooltennisreservationapi.model.mappers.CourtSurfaceMapper;
import com.example.inqooltennisreservationapi.repository.CourtSurfaceRepository;
import com.example.inqooltennisreservationapi.service.CourtSurfaceService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourtSurfaceServiceImpl implements CourtSurfaceService {

    private final CourtSurfaceRepository surfaceRepository;
    private final CourtSurfaceMapper courtSurfaceMapper;

    @Autowired
    public CourtSurfaceServiceImpl(CourtSurfaceRepository courtSurfaceRepository, CourtSurfaceMapper mapper) {
        this.surfaceRepository = courtSurfaceRepository;
        this.courtSurfaceMapper = mapper;
    }

    @Override
    @Transactional
    public CourtSurfaceDTOs.CourtSurfaceResponseDTO getSurface(long id) {
        var entity = surfaceRepository.getCourtSurfaceById(id);
        if (entity.isEmpty()) {
            throw new EntityNotFoundException(String.format("Court surface with id %s not found", id));
        }
        return courtSurfaceMapper.entityToResponseDto(entity.get());
    }


    @Override
    @Transactional
    public CourtSurfaceDTOs.CourtSurfaceResponseDTO createSurface(CourtSurfaceDTOs.CourtSurfaceModifyParams surface) {
        var entityToSave = courtSurfaceMapper.dtoToEntity(surface);

        var result = surfaceRepository.createCourtSurface(entityToSave);
        if (result.isEmpty()) {
            throw new DatabaseException("Court surface creation failed");
        }
        return courtSurfaceMapper.entityToResponseDto(result.get());
    }

    @Override
    @Transactional
    public CourtSurfaceDTOs.CourtSurfaceResponseDTO editSurface(long id, CourtSurfaceDTOs.CourtSurfaceModifyParams surface) {
        var entityToSave = courtSurfaceMapper.dtoToEntity(surface);
        entityToSave.setId(id);

        if (!surfaceExists(id)) {
            throw new EntityNotFoundException(String.format("Court surface with id %s not found", id));
        }

        var result = surfaceRepository.updateCourtSurface(id, entityToSave);
        if (result.isEmpty()) {
            throw new DatabaseException("Court surface update failed");
        }
        return courtSurfaceMapper.entityToResponseDto(result.get());
    }

    @Override
    @Transactional
    public CourtSurfaceDTOs.CourtSurfaceResponseDTO deleteSurface(long id) {
        if (!surfaceExists(id)) {
            throw new EntityNotFoundException(String.format("Court surface with id %s not found", id));
        }
        var result = surfaceRepository.deleteCourtSurface(id);
        if (result.isEmpty()) {
            throw new DatabaseException("Court surface delete failed");
        }
        return courtSurfaceMapper.entityToResponseDto(result.get());
    }

    @Override
    @Transactional
    public List<CourtSurfaceDTOs.CourtSurfaceResponseDTO> getAllSurfaces() {
        var res = surfaceRepository.listCourtSurfaces();
        return res.stream().map(courtSurfaceMapper::entityToResponseDto).toList();
    }

    @Transactional
    protected boolean surfaceExists(long id) {
        return surfaceRepository.getCourtSurfaceById(id).isPresent();
    }
}
