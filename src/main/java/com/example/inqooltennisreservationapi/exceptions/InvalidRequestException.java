package com.example.inqooltennisreservationapi.exceptions;

public class InvalidRequestException extends ExceptionWithMessagePrefix {

    private static final String MESSAGE_PREFIX = "Invalid request";

    public InvalidRequestException() {
        super(MESSAGE_PREFIX);
    }

    public InvalidRequestException(String message) {
        super(MESSAGE_PREFIX, message);
    }
}
