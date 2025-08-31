package com.reliaquest.api.exception;

/**
 * @author Rohit Bothe
 */
public class EmployeeInputException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EmployeeInputException(String errorMessage) {
        super(errorMessage);
    }
}
