package com.workshop.vehicle.vehicle_service.application.exceptions;

import jdk.jshell.spi.ExecutionControl;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.validation.FieldError;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    void testHandleIllegalArgument() {
        IllegalArgumentException exception = new IllegalArgumentException("Test message");
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleIllegalArgument(exception, mock(WebRequest.class)).block();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid JSON format: Test message", response.getBody().getMessage());
    }

    @Test
    void testHandleHttpMessageConversionException() {
        HttpMessageConversionException exception = new HttpMessageConversionException("Test message");
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleHttpMessageConversionException(exception, mock(WebRequest.class)).block();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid JSON format: Test message", response.getBody().getMessage());
    }

    @Test
    void testHandleNumberFormatException() {
        NumberFormatException exception = new NumberFormatException("Test message");
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleNumberFormatException(exception, mock(WebRequest.class)).block();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid input", response.getBody().getMessage());
    }

    @Test
    void testHandleMethodArgumentNotValid() {
        List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(new FieldError("objectName", "fieldName", "Field is invalid"));

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleMethodArgumentNotValid(exception).block();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation failed: Field is invalid; ", response.getBody().getMessage());
    }

    @Test
    void testHandleNoSuchElementException() {
        NoSuchElementException exception = new NoSuchElementException("Test message");
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleNoSuchElementException(exception, mock(WebRequest.class)).block();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Test message", response.getBody().getMessage());
    }

    @Test
    void testHandleInternalException() {
        ExecutionControl.InternalException exception = new ExecutionControl.InternalException("Test message");
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleInternalException(exception, mock(WebRequest.class)).block();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Test message", response.getBody().getMessage());
    }
}
