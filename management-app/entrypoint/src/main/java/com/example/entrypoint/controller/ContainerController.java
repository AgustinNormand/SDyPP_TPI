package com.example.entrypoint.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/image")
public class ContainerController {


    @PostMapping
    public ResponseEntity<String> process(@RequestBody Object input) {
        return new ResponseEntity<>("FEATURE_NOT_IMPLEMENTED", HttpStatus.BAD_REQUEST);
    }

}