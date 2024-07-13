package com.example.inqooltennisreservationapi.model.api;

import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

public record ApiErrorDTO(HttpStatusCode status, String message, LocalDateTime timestamp) {
    public ApiErrorDTO(HttpStatusCode status, String message) {
        this(status, message, LocalDateTime.now());
    }
}