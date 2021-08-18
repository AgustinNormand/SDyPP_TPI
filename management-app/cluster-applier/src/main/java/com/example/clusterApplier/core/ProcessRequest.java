package com.example.clusterApplier.core;

import lombok.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * A request to be processed by a {@link CoreProcessor} containing a splitter, a joiner and a worker {@link File}
 */
@Data
public class ProcessRequest {

    private String jobId;

    private List<Script> pending;

    private List<Script> done;

    public ProcessRequest() {
        this.pending = new ArrayList<>();
        this.done = new ArrayList<>();
    }

    /**
     * Checks if the request is valid to be processed
     * @return true if it's valid, false if it isn't
     */
    public boolean isValid() {
        return !(pending.isEmpty() && done.isEmpty());
    }

    public void scriptProcessed(Script script) {
        this.getPending().remove(script);
        this.getDone().add(script);
    }
}
