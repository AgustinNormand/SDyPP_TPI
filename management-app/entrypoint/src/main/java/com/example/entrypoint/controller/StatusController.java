package com.example.entrypoint.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/status")
public class StatusController {


    @GetMapping
    public ResponseEntity process(@RequestBody Object input) {
        System.out.println(input);
        return ResponseEntity.ok().build();
    }

}