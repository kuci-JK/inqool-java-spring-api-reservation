package com.example.inqooltennisreservationapi.exceptions;

public class EntityNotFoundException extends ExceptionWithMessagePrefix {

    private static final String MESSAGE_PREFIX = "Entity not found";

    public EntityNotFoundException() {
        super(MESSAGE_PREFIX);
    }

    public EntityNotFoundException(String message) {
        super(MESSAGE_PREFIX, message);
    }

}
