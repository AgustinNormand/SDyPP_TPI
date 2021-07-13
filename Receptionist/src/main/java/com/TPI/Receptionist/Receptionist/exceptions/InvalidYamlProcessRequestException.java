package com.TPI.Receptionist.Receptionist.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidYamlProcessRequestException extends RuntimeException {
    public InvalidYamlProcessRequestException() {
    }

    public InvalidYamlProcessRequestException(String message) {
        super(message);
    }
}
