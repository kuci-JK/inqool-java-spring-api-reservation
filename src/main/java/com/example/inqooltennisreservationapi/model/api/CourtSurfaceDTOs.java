package com.example.inqooltennisreservationapi.model.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CourtSurfaceDTOs {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CourtSurfaceModifyParams {
        private String surfaceName;
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
