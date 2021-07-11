package com.TPI.Server.Servidor.core;

import lombok.Data;

import java.io.File;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * A request to be processed by a {@link CoreProcessor} containing a splitter, a joiner and a worker {@link File}
 */
@Data
public class ProcessRequest {

    private Script splitter;

    private Script worker;

    private Script joiner;

    /**
     * Checks if the request is valid to be processed
     * @return true if it's valid, false if it isn't
     */
    public boolean isValid() {
        return Stream.of(splitter, worker, joiner).noneMatch(Objects::isNull);
    }

}
