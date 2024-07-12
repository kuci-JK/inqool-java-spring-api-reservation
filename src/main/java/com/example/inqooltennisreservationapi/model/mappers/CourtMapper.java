package com.example.inqooltennisreservationapi.model.mappers;

import com.example.inqooltennisreservationapi.exceptions.EntityNotFoundException;
import com.example.inqooltennisreservationapi.model.api.CourtDTOs;
import com.example.inqooltennisreservationapi.model.entity.CourtEntity;
import com.example.inqooltennisreservationapi.repository.CourtSurfaceRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CourtMapper {

    private final CourtSurfaceRepository surfaceRepo;
    private final CourtSurfaceMapper surfaceMapper;

    @Autowired
    public CourtMapper(CourtSurfaceRepository surfaceRepo, CourtSurfaceMapper surfaceMapper) {
        this.surfaceRepo = surfaceRepo;
        this.surfaceMapper = surfaceMapper;
    }

    public CourtDTOs.CourtResponseDTO entityToResponseDto(@NotNull CourtEntity court) {
        return new CourtDTOs.CourtResponseDTO(
                court.getId(),
                court.getName(),
                surfaceMapper.entityToResponseDto(court.getSurface()),
                court.isDeleted()
        );
    }

    public CourtEntity dtoToEntity(CourtDTOs.@NotNull CourtModifyParams courtModifyParams) {

        var surface = surfaceRepo.getCourtSurfaceById(courtModifyParams.getCourtSurfaceId());
        if (surface.isEmpty()) {
            throw new EntityNotFoundException(String.format("Court surface with id %s not found", courtModifyParams.getCourtSurfaceId()));
        }
        return surface.map(courtSurfaceEntity -> new CourtEntity(0, courtModifyParams.getCourtName(), courtSurfaceEntity)).get();
    }


}
