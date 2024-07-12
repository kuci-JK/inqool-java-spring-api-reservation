package com.example.inqooltennisreservationapi.model.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UserDTOs {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserModifyParams {

        @NotBlank(message = "Name cannot be blank")
        private String name;

        @NotBlank(message = "Phone number cannot be blank")
        @Size(min = 3, max = 20, message = "Phone number should be between 3 and 20 characters")
        @Pattern(regexp = "^[+]?[0-9]+$", message = "Invalid phone number")
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
