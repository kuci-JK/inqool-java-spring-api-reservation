package com.example.inqooltennisreservationapi.model.mappers;

import com.example.inqooltennisreservationapi.model.api.CourtDTOs;
import com.example.inqooltennisreservationapi.model.entity.CourtEntity;
import com.example.inqooltennisreservationapi.repository.CourtSurfaceRepository;
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

    public CourtDTOs.CourtResponseDTO entityToResponseDto(CourtEntity court) {
        if (court == null) {
            return null; // TODO
        }
        return new CourtDTOs.CourtResponseDTO(
                court.getId(),
                court.getName(),
                surfaceMapper.entityToResponseDto(court.getSurface()),
                court.isDeleted()
        );
    }

    public CourtEntity dtoToEntity(CourtDTOs.CourtModifyParams courtModifyParams) {
        if (courtModifyParams == null) {
            return null; // TODO
        }

        var surface = surfaceRepo.getCourtSurfaceById(courtModifyParams.getCourtSurfaceId());
        if (surface.isEmpty()) {
            return null; // TODO maybe throw something
        }
        return surface.map(courtSurfaceEntity -> new CourtEntity(0, courtModifyParams.getCourtName(), courtSurfaceEntity)).get();
    }


}
