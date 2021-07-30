package com.example.commons.dto;

import lombok.Data;

@Data
public class JobStatus {

    State state;

    ProcessResultDto result;

    public enum State {
        PENDING, VALIDATING_YAML, APPLYING_IN_CLUSTER, DONE
    }

}
