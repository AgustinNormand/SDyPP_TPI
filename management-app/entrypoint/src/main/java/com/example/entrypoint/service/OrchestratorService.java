package com.example.entrypoint.service;

import com.example.commons.dto.JobResponse;
import com.example.commons.dto.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class OrchestratorService {

    @Autowired
    UploadService uploadService;

    @Autowired
    JobNotifierService jobNotifier;

    public JobResponse process(Serializable payload, String outputQueue) {
        JobResponse jobResponse = JobResponse.build();

        String uploadedResource = uploadService.upload(jobResponse.getJobId(), payload);

        Task task = new Task(jobResponse.getJobId(), uploadedResource);

        jobNotifier.jobCreated(outputQueue, task);
        return jobResponse;
    }

}