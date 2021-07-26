package com.example.commons.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobResponse {

    @JsonProperty("job_id")
    private String jobId;

    public static JobResponse build() {
        JobResponse response = new JobResponse();
        response.setJobId(UUID.randomUUID().toString());
        return response;
    }
}
