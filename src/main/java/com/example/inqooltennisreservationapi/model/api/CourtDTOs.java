package com.example.inqooltennisreservationapi.model.api;

import lombok.AllArgsConstructor;
import lombok.Data;

public class CourtDTOs {

    @Data
    @AllArgsConstructor
    public static class CourtModifyParams {
        private String courtName;
        private long courtSurfaceId;
    }

    @Data
    @AllArgsConstructor
    public static class CourtResponseDTO {
        private long id;
        private String courtName;
        private CourtSurfaceDTOs.CourtSurfaceResponseDTO surface;
        private boolean deleted;
    }

}
