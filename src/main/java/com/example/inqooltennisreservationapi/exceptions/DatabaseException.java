package com.example.inqooltennisreservationapi.exceptions;

public class DatabaseException extends ExceptionWithMessagePrefix {

    private static final String MESSAGE_PREFIX = "Database error";

    public DatabaseException() {
        super(MESSAGE_PREFIX);
    }

    public DatabaseException(String message) {
        super(MESSAGE_PREFIX, message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(MESSAGE_PREFIX, message, cause);
    }

    public DatabaseException(Throwable cause) {
        super(MESSAGE_PREFIX, cause);
    }

}
