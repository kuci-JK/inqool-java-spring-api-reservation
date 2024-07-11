package com.example.inqooltennisreservationapi.model.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CourtDTOs {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CourtModifyParams {
        private String courtName;
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
