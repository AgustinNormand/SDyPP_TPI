package com.example.entrypoint.controller;

import com.example.entrypoint.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/status")
public class StatusController {

    @Autowired
    private StatusService statusService;

    @GetMapping("/{jobId}")
    public ResponseEntity<Object> process(@PathVariable String jobId) {
        Object response = statusService.getJobStatus(jobId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}