package com.example.inqooltennisreservationapi.model.mappers;

import com.example.inqooltennisreservationapi.model.api.CourtSurfaceDTOs;
import com.example.inqooltennisreservationapi.model.entity.CourtSurfaceEntity;
import org.springframework.stereotype.Component;

@Component
public class CourtSurfaceMapper {

    public CourtSurfaceDTOs.CourtSurfaceResponseDTO entityToResponseDto(CourtSurfaceEntity courtSurface) {
        if (courtSurface == null) {
            return null;
        }
        return new CourtSurfaceDTOs.CourtSurfaceResponseDTO(courtSurface.getId(), courtSurface.getSurfaceName(), courtSurface.getPricePerMinute(), courtSurface.isDeleted());
    }


    public CourtSurfaceEntity dtoToEntity(CourtSurfaceDTOs.CourtSurfaceModifyParams courtSurfaceModifyParams) {
        if (courtSurfaceModifyParams == null) {
            return null;
        }
        return new CourtSurfaceEntity(0, courtSurfaceModifyParams.getSurfaceName(), courtSurfaceModifyParams.getPricePerMinute());
    }

}
