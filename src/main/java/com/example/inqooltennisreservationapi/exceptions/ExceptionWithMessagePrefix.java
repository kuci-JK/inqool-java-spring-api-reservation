package com.example.inqooltennisreservationapi.exceptions;

public class ExceptionWithMessagePrefix extends RuntimeException {


    public ExceptionWithMessagePrefix(String messagePrefix) {
        super(messagePrefix);
    }

    public ExceptionWithMessagePrefix(String messagePrefix, String message) {
        super(applyMessagePrefix(messagePrefix, message));
    }

    private static String applyMessagePrefix(String prefix, String message) {
        return prefix + ": " + message;
    }

}
