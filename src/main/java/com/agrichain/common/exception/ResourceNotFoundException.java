package com.agrichain.common.exception;

// Thrown when a requested resource does not exist in the database.
// e.g. GET /api/v1/farmers/999 where farmer 999 doesn't exist.
// Maps to HTTP 404 Not Found in the GlobalExceptionHandler.
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
