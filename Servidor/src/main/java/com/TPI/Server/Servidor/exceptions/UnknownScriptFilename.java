package com.TPI.Server.Servidor.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnknownScriptFilename extends RuntimeException {
    public UnknownScriptFilename(String filename) {
        super(String.format("The filename %s is not within the allowed resource identifiers", filename));
    }
}
