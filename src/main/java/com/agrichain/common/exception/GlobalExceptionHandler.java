package com.agrichain.common.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ─── Business Logic Exceptions ───────────────────────────────────────────────

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiErrorResponse(LocalDateTime.now(), 404, "Not Found", ex.getMessage())
        );
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicate(DuplicateResourceException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ApiErrorResponse(LocalDateTime.now(), 409, "Duplicate Entry", ex.getMessage())
        );
    }

    // Thrown for business rule violations: wrong role assigned, invalid status transition, etc.
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiErrorResponse(LocalDateTime.now(), 400, "Bad Request", ex.getMessage())
        );
    }

    // ─── Spring Security Exceptions ──────────────────────────────────────────────

    // Thrown by Spring Security when email or password is wrong at login
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ApiErrorResponse(LocalDateTime.now(), 401, "Unauthorized", "Invalid email or password.")
        );
    }

    // Thrown when an authenticated user hits an endpoint their role cannot access
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDenied(AuthorizationDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ApiErrorResponse(LocalDateTime.now(), 403, "Forbidden", "You do not have permission to access this resource.")
        );
    }

    // ─── Request / Validation Exceptions ─────────────────────────────────────────

    // Thrown when @Valid fails on a request body — collects all field errors into one message
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiErrorResponse(LocalDateTime.now(), 400, "Bad Request", message)
        );
    }

    // Thrown when the request body is malformed JSON or contains an invalid enum value
    // e.g. sending "status": "SHIPPED" when the valid values are PENDING, CONFIRMED, DELIVERED, CANCELLED
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleMessageNotReadable(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiErrorResponse(LocalDateTime.now(), 400, "Bad Request", "Invalid value in request body. Please check your fields and try again.")
        );
    }

    // Thrown when the client uses the wrong HTTP method — e.g. GET on a POST-only endpoint
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(
                new ApiErrorResponse(LocalDateTime.now(), 405, "Method Not Allowed",
                        "HTTP method '" + ex.getMethod() + "' is not supported for this endpoint.")
        );
    }

    // Thrown when the client hits a URL that does not exist in the application
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNoResourceFound(NoResourceFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiErrorResponse(LocalDateTime.now(), 404, "Not Found", "The endpoint you are trying to reach does not exist.")
        );
    }

    // Thrown when the client sends a request with the wrong Content-Type (e.g. XML instead of JSON)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(
                new ApiErrorResponse(LocalDateTime.now(), 415, "Unsupported Media Type",
                        "Content-Type '" + ex.getContentType() + "' is not supported. Please send JSON with Content-Type: application/json.")
        );
    }

    // ─── Database Exceptions ──────────────────────────────────────────────────────

    // Thrown when a DB unique constraint is violated that wasn't caught at the service level
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ApiErrorResponse(LocalDateTime.now(), 409, "Duplicate Entry", "A record with the same unique value already exists.")
        );
    }

    // ─── Catch-All ────────────────────────────────────────────────────────────────

    // Anything not matched above lands here — never expose internal details to the client
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ApiErrorResponse(LocalDateTime.now(), 500, "Internal Server Error", "An unexpected error occurred. Please try again later.")
        );
    }
}
