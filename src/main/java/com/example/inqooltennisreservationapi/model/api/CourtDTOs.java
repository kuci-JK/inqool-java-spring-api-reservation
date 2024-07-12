package com.example.inqooltennisreservationapi.model.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CourtDTOs {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CourtModifyParams {
        @NotBlank(message = "Name cannot be empty")
        @Size(min = 1, message = "Name mus have at least one character")
        private String courtName;
        @Positive(message = "Invalid courtSurfaceId")
        private long courtSurfaceId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CourtResponseDTO {
        private long id;
        private String courtName;
        private CourtSurfaceDTOs.CourtSurfaceResponseDTO surface;
        private boolean deleted;
    }

}
