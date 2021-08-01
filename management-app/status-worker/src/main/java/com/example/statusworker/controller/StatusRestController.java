package com.example.statusworker.controller;

import com.example.commons.dto.StepStatus;
import com.example.statusworker.service.StatusRetrieverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/status")
public class StatusRestController {

    @Autowired
    StatusRetrieverService service;

    private Logger logger = LoggerFactory.getLogger(StatusRestController.class);

    @GetMapping("/{jobId}")
    public ResponseEntity<List<StepStatus>> getJobStatus(@PathVariable String jobId) {
        logger.info("Get job status request {}", jobId);
        List<StepStatus> result = service.getJobStatus(jobId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
