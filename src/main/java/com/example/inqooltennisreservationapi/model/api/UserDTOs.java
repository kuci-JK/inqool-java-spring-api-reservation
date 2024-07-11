package com.example.inqooltennisreservationapi.model.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UserDTOs {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserModifyParams {
        private String name;
        private String phone;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserResponseDTO {
        private long id;
        private String name;
        private String phone;
        private boolean deleted;
    }
}
