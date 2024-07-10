package com.example.inqooltennisreservationapi.model.api;

import lombok.AllArgsConstructor;
import lombok.Data;

public class UserDTOs {

    @Data
    @AllArgsConstructor
    public static class UserModifyParams {
        private String name;
        private String phone;
    }

    @Data
    @AllArgsConstructor
    public static class UserResponseDTO {
        private long id;
        private String name;
        private String phone;
        private boolean deleted;
    }
}
