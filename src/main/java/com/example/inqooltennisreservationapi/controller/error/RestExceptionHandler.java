package com.example.inqooltennisreservationapi.controller.error;

import com.example.inqooltennisreservationapi.exceptions.DatabaseException;
import com.example.inqooltennisreservationapi.exceptions.EntityNotFoundException;
import com.example.inqooltennisreservationapi.exceptions.InvalidRequestException;
import com.example.inqooltennisreservationapi.model.api.ApiErrorDTO;
import jakarta.validation.ConstraintViolationException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.info("Missing path variable: {}", ex.getMessage());
        return getResponseEntity("Missing path variable", status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.info("HTTP request method not supported: {}", ex.getMessage());
        return getResponseEntity("HTTP request method not supported", status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.info("Method Argument Validation failed: {}", ex.getMessage());
        return getResponseEntity("Validation failed", status);
    }


    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.info("No resource found: {}", ex.getMessage());
        return getResponseEntity("No resource found", status);
    }

    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.info("Conversion not supported: {}", ex.getMessage());
        return getResponseEntity("Conversion not supported", status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.info("HTTP message not readable: {}", ex.getMessage());
        return getResponseEntity("HTTP message not readable", status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodValidationException(MethodValidationException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("Method Validation failed: {}", ex.getMessage());
        return getResponseEntity("Validation failed", status);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        log.info("Entity not found: {}", ex.getMessage());
        return getResponseEntityFromException(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DatabaseException.class)
    protected ResponseEntity<Object> handleDataAccessException(DatabaseException ex) {
        log.info("Database exception: {}", ex.getMessage());
        return getResponseEntityFromException(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidRequestException.class)
    protected ResponseEntity<Object> handleInvalidRequest(InvalidRequestException ex) {
        log.info("Invalid request: {}", ex.getMessage());
        return getResponseEntityFromException(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        log.info("Constraint validation failed: {}", ex.getMessage());
        return getResponseEntity("Constraint validation failed", HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> getResponseEntity(String message, HttpStatusCode statusCode) {
        return buildResponseEntity(new ApiErrorDTO(statusCode, message));
    }

    private ResponseEntity<Object> getResponseEntityFromException(@NotNull Exception ex, HttpStatusCode statusCode) {
        return buildResponseEntity(new ApiErrorDTO(statusCode, ex.getMessage()));
    }


    private ResponseEntity<Object> buildResponseEntity(@NotNull ApiErrorDTO apiError) {
        return new ResponseEntity<>(apiError, apiError.status());
    }

}
