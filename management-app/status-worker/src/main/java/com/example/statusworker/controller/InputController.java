package com.example.statusworker.controller;

import com.example.commons.dto.Task;
import com.example.commons.dto.YamlProcessResult;
import com.example.statusworker.events.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Consumer;

@RestController
public class InputController {

    private Logger logger = LoggerFactory.getLogger(InputController.class);

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Bean
    public Consumer<Task> yamlForValidation() {
        return request -> {
            logger.info("Received yaml for validation: {}", request);
            applicationEventPublisher.publishEvent(new YamlForValidationEvent(request));
        };
    }

    @Bean
    public Consumer<Task> yamlValidated() {
        return request -> {
            logger.info("Received validated yaml: {}", request);
            applicationEventPublisher.publishEvent(new YamlValidatedEvent(request));
        };
    }

    @Bean
    public Consumer<YamlProcessResult> yamlProcessed() {
        return request -> {
            logger.info("Received processed yaml: {}", request);
            applicationEventPublisher.publishEvent(new YamlProcessedEvent(request));
        };
    }


}