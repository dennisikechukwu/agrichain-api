package com.agrichain.common.exception;

// Thrown when someone tries to create something that already exists.
// e.g. registering with an email that's already in the database.
// Maps to HTTP 409 Conflict in the GlobalExceptionHandler.
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
