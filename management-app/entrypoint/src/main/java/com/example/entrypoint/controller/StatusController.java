package com.example.entrypoint.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/status")
public class StatusController {


    @PostMapping
    public ResponseEntity<String> process(@RequestBody Object input) {
        return new ResponseEntity<>("FEATURE_NOT_IMPLEMENTED", HttpStatus.BAD_REQUEST);
    }

}