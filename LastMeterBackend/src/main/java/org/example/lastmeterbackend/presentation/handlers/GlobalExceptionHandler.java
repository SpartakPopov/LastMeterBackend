package org.example.lastmeterbackend.presentation.handlers;

import org.example.lastmeterbackend.exceptions.PackageNotFoundException;
import org.example.lastmeterbackend.exceptions.PackageStateConflictException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PackageNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(PackageNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(PackageStateConflictException.class)
    public ResponseEntity<Map<String, Object>> handleConflict(PackageStateConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "error", ex.getMessage(),
                        "currentStatus", ex.getCurrentStatus().name()
                ));
    }
}
