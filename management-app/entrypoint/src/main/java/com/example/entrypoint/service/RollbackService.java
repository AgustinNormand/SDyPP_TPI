package com.example.entrypoint.service;

import com.example.commons.dto.JobResponse;
import com.example.commons.dto.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RollbackService {

    private static final String OUTPUT_QUEUE = "rollback-tasks";

    @Autowired
    JobNotifierService jobNotifierService;

    public JobResponse rollback(String jobId) {
        JobResponse response = JobResponse.build();

        // Generate task to rollback job identified by given job id
        Task task = new Task(jobId);
        jobNotifierService.jobCreated(OUTPUT_QUEUE, task);

        return response;
    }
}
