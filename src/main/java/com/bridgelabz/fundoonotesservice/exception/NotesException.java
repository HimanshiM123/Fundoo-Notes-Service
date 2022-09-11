package com.bridgelabz.fundoonotesservice.exception;

public class NotesException extends RuntimeException {
    private int statusCode;
    private String statusMessage;

    public NotesException(int statusCode, String statusMessage) {
        super(statusMessage);
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }
}
