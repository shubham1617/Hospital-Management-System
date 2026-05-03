package com.pm.doctorservice.exception;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Custom exception for invalid input")
public class InvalidInputException extends RuntimeException {

    public InvalidInputException(String message) {
        super(message);
    }

    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
