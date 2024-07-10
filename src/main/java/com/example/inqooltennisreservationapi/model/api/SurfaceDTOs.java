package com.example.inqooltennisreservationapi.model.api;

import lombok.AllArgsConstructor;
import lombok.Data;

public class SurfaceDTOs {

    @Data
    @AllArgsConstructor
    public static class SurfaceModifyParams {
        private String surfaceName;
        private double pricePerMinute;
    }

    @Data
    @AllArgsConstructor
    public static class SurfaceResponseDTO {
        private long id;
        private String surfaceName;
        private double pricePerMinute;
        private boolean deleted;
    }

}
