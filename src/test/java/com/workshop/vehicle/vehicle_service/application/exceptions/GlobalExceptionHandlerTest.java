package com.workshop.vehicle.vehicle_service.application.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    void handleIllegalArgument_ShouldReturnBadRequestResponse() {
        // Arrange
        IllegalArgumentException exception = new IllegalArgumentException("Invalid ID format: invalid-id");

        // Act
        ResponseEntity<String> response = globalExceptionHandler.handleIllegalArgument(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid ID format: invalid-id", response.getBody());
    }
}
