package com.TPI.Server.Servidor.exceptions;

import com.TPI.Server.Servidor.core.ProcessRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidProcessRequestException extends RuntimeException {
    public InvalidProcessRequestException(ProcessRequest processRequest) {
        super(String.format("The request %s cannot be processed", processRequest));
    }
}
