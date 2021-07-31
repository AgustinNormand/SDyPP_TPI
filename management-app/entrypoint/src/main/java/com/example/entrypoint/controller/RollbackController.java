package com.example.entrypoint.controller;

import com.example.commons.dto.JobResponse;
import com.example.entrypoint.service.RollbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rollback")
public class RollbackController {

    @Autowired
    RollbackService rollbackService;

    private Logger logger = LoggerFactory.getLogger(RollbackController.class);

    @PostMapping("/{jobId}")
    public ResponseEntity<JobResponse> rollback(@PathVariable String jobId) {
        logger.info("Rollback request for job {}", jobId);
        JobResponse jobResponse = rollbackService.rollback(jobId);
        return new ResponseEntity<>(jobResponse, HttpStatus.OK);
    }

}
