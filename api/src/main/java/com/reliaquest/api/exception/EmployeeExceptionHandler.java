package com.reliaquest.api.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.reliaquest.api.constant.EmployeeConstants;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import jakarta.validation.ConstraintViolationException;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException.TooManyRequests;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author Rohit Bothe
 */
@ControllerAdvice
public class EmployeeExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(EmployeeExceptionHandler.class);

    @ExceptionHandler(value = {TooManyRequests.class})
    protected ResponseEntity<Object> handleTooManyRequestErrorHandler(RuntimeException ex, WebRequest request) {
        log.error(EmployeeConstants.ERROR_TOO_MANY_REQUEST_MESSAGE);
        return handleExceptionInternal(
                ex,
                EmployeeConstants.ERROR_TOO_MANY_REQUEST_MESSAGE,
                new HttpHeaders(),
                HttpStatus.TOO_MANY_REQUESTS,
                request);
    }

    @ExceptionHandler(value = {JsonMappingException.class})
    protected ResponseEntity<Object> JSONMappingErrorHandler(RuntimeException ex, WebRequest request) {
        log.error(EmployeeConstants.ERROR_JSON_MAPPING_MESSAGE);
        return handleExceptionInternal(
                ex,
                EmployeeConstants.ERROR_JSON_MAPPING_MESSAGE,
                new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                request);
    }

    @ExceptionHandler(value = {JsonProcessingException.class})
    protected ResponseEntity<Object> JSONProcessingErrorHandler(RuntimeException ex, WebRequest request) {
        log.error(EmployeeConstants.ERROR_JSON_PROCESSING_MESSAGE);
        return handleExceptionInternal(
                ex,
                EmployeeConstants.ERROR_JSON_PROCESSING_MESSAGE,
                new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                request);
    }

    @ExceptionHandler(value = {IOException.class})
    protected ResponseEntity<Object> IOExceptionHandler(RuntimeException ex, WebRequest request) {
        log.error(EmployeeConstants.ERROR_IO_EXCEPTION_MESSAGE);
        return handleExceptionInternal(
                ex,
                EmployeeConstants.ERROR_IO_EXCEPTION_MESSAGE,
                new HttpHeaders(),
                HttpStatus.SERVICE_UNAVAILABLE,
                request);
    }

    @ExceptionHandler(value = {EmployeeInputException.class})
    protected ResponseEntity<Object> EmployeeInputExceptionHandler(RuntimeException ex, WebRequest request) {
        log.error(EmployeeConstants.ERROR_EMPLOYEE_INPUT_MESSAGE);
        return handleExceptionInternal(
                ex,
                EmployeeConstants.ERROR_EMPLOYEE_INPUT_MESSAGE,
                new HttpHeaders(),
                HttpStatus.EXPECTATION_FAILED,
                request);
    }

    @ExceptionHandler(value = {InternalServerError.class})
    protected ResponseEntity<Object> InternalServerErrorHandler(RuntimeException ex, WebRequest request) {
        log.error(EmployeeConstants.ERROR_INTERNAL_SERVER);
        return handleExceptionInternal(
                ex, EmployeeConstants.ERROR_INTERNAL_SERVER, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED, request);
    }

    @ExceptionHandler(value = {CallNotPermittedException.class})
    protected ResponseEntity<Object> handleRestClientError(CallNotPermittedException ex, WebRequest request) {
        log.error(EmployeeConstants.TO_MANY_REQUEST_MSG);
        return handleExceptionInternal(
                ex, EmployeeConstants.TO_MANY_REQUEST_MSG, new HttpHeaders(), HttpStatus.TOO_MANY_REQUESTS, request);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        log.error(EmployeeConstants.VALIDATION_FAILED);
        return handleExceptionInternal(
                ex, EmployeeConstants.VALIDATION_FAILED, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {RestClientResponseException.class})
    protected ResponseEntity<Object> handleRestClientError(RestClientResponseException ex, WebRequest request) {
        log.error(EmployeeConstants.TO_MANY_REQUEST_MSG);
        return ResponseEntity.status(ex.getStatusCode()).body(ex);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleGeneralError(Exception ex, WebRequest request) {
        log.error(EmployeeConstants.ERROR_INTERNAL_SERVER);
        return handleExceptionInternal(
                ex, EmployeeConstants.ERROR_INTERNAL_SERVER, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED, request);
    }
}
