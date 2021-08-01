package com.example.entrypoint.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StatusService {

    @Value("${status.service.url}")
    private String URL;

    public Object getJobStatus(String jobId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = URL + "/status/" + jobId;
        ResponseEntity<Object> response = restTemplate.getForEntity(url, Object.class);
        return response.getBody();
    }
}