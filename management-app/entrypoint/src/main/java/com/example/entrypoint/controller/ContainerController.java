package com.example.entrypoint.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/image")
public class ContainerController {


    @PostMapping
    public ResponseEntity process(@RequestBody Object input) {
        System.out.println(input);
        return ResponseEntity.ok().build();
    }

}