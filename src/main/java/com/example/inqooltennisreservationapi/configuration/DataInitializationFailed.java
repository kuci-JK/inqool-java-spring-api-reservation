package com.example.inqooltennisreservationapi.configuration;

public class DataInitializationFailed extends RuntimeException {
    public DataInitializationFailed() {
        super("Data initialization failed");
    }
}
