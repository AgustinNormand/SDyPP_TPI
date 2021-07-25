package com.example.splitter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InputController {


    @PostMapping("/receive")
    public ResponseEntity process(@RequestBody Object input) {
        System.out.println(input);
        return ResponseEntity.ok().build();
    }

}