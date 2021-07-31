package com.example.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    private String jobId;

    private String blobName;

    public Task(String jobId) {
        this.jobId = jobId;
    }

}