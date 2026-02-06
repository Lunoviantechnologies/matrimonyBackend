package com.example.matrimony.config;

import com.example.matrimony.exception.EmailAlreadyExistsException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ------------------------------------------------
    // 🔴 CUSTOM BUSINESS EXCEPTIONS
    // ------------------------------------------------

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleEmailAlreadyExists(
            EmailAlreadyExistsException ex) {

        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    // ------------------------------------------------
    // 🔴 ILLEGAL ARGUMENT / BUSINESS VALIDATION
    // ------------------------------------------------

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(
            IllegalArgumentException ex) {

        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // ------------------------------------------------
    // 🔴 @VALID REQUEST BODY VALIDATION ERRORS
    // ------------------------------------------------

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        Map<String, String> fieldErrors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error ->
                        fieldErrors.put(error.getField(), error.getDefaultMessage()));

        Map<String, Object> body = baseBody(HttpStatus.BAD_REQUEST);
        body.put("errors", fieldErrors);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(body);
    }

    // ------------------------------------------------
    // 🔴 PATH / PARAM VALIDATION ERRORS
    // ------------------------------------------------

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(
            ConstraintViolationException ex) {

        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // ------------------------------------------------
    // 🔴 FALLBACK – HANDLE ANY UNEXPECTED ERROR
    // ------------------------------------------------

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAllExceptions(
            Exception ex) {

        ex.printStackTrace(); // 🔴 keep for debugging (replace with logger in prod)

        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Something went wrong. Please try again later."
        );
    }

    // ------------------------------------------------
    // 🔹 COMMON RESPONSE BUILDERS
    // ------------------------------------------------

    private ResponseEntity<Map<String, Object>> buildResponse(
            HttpStatus status, String message) {

        Map<String, Object> body = baseBody(status);
        body.put("message", message);

        return ResponseEntity.status(status).body(body);
    }

    private Map<String, Object> baseBody(HttpStatus status) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("timestamp", Instant.now());
        return body;
    }
}
