package com.example.statusworker.service;

import com.example.commons.dto.JobStatus;
import com.example.commons.dto.StepStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatusRetrieverService {

    @Autowired
    RedisService redisService;

    public List<StepStatus> getJobStatus(String jobId) {
        return Arrays.stream(JobStatus.State.values())
                .map(state -> {
                    String stepContent = redisService.getStep(jobId, state);
                    return new StepStatus(state, stepContent);
                })
                .collect(Collectors.toList());
    }
}
