package com.example.clusterApplier.core.exceptions;

import com.example.clusterApplier.core.ProcessRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidProcessRequestException extends RuntimeException {
    public InvalidProcessRequestException(ProcessRequest processRequest) {
        super(String.format("The request %s cannot be processed", processRequest));
    }
}
