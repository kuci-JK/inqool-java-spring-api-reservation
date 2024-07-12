package com.example.inqooltennisreservationapi.model.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CourtSurfaceDTOs {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CourtSurfaceModifyParams {
        @NotBlank(message = "surfaceName must be provided")
        @Size(min = 1, message = "surfaceName must have at least one character")
        private String surfaceName;

        @Positive(message = "Price cannot be negative")
        private double pricePerMinute;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CourtSurfaceResponseDTO {
        private long id;
        private String surfaceName;
        private double pricePerMinute;
        private boolean deleted;
    }

}
