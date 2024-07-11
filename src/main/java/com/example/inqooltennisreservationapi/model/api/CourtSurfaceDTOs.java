package com.example.inqooltennisreservationapi.model.api;

import lombok.AllArgsConstructor;
import lombok.Data;

public class CourtSurfaceDTOs {

    @Data
    @AllArgsConstructor
    public static class CourtSurfaceModifyParams {
        private String surfaceName;
        private double pricePerMinute;
    }

    @Data
    @AllArgsConstructor
    public static class CourtSurfaceResponseDTO {
        private long id;
        private String surfaceName;
        private double pricePerMinute;
        private boolean deleted;
    }

}
