package com.example.inqooltennisreservationapi.model.mappers;

import com.example.inqooltennisreservationapi.model.api.CourtSurfaceDTOs;
import com.example.inqooltennisreservationapi.model.entity.CourtSurfaceEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class CourtSurfaceMapper {

    public @NotNull CourtSurfaceDTOs.CourtSurfaceResponseDTO entityToResponseDto(@NotNull CourtSurfaceEntity courtSurface) {
        return new CourtSurfaceDTOs.CourtSurfaceResponseDTO(
                courtSurface.getId(),
                courtSurface.getSurfaceName(),
                courtSurface.getPricePerMinute(),
                courtSurface.isDeleted()
        );
    }


    public @NotNull CourtSurfaceEntity dtoToEntity(CourtSurfaceDTOs.@NotNull CourtSurfaceModifyParams courtSurfaceModifyParams) {
        return new CourtSurfaceEntity(0, courtSurfaceModifyParams.getSurfaceName(), courtSurfaceModifyParams.getPricePerMinute());
    }

}
