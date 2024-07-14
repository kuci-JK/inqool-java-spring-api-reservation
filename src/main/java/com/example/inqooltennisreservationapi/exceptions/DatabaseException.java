package com.example.inqooltennisreservationapi.exceptions;

public class DatabaseException extends ExceptionWithMessagePrefix {

    private static final String MESSAGE_PREFIX = "Database error";

    public DatabaseException() {
        super(MESSAGE_PREFIX);
    }

    public DatabaseException(String message) {
        super(MESSAGE_PREFIX, message);
    }
}
