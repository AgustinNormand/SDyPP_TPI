package com.example.clusterApplier.controller;

import com.example.clusterApplier.ClusterApplierApplication;
import com.example.clusterApplier.events.YamlAppliedEvent;
import com.example.clusterApplier.events.RequestReceivedEvent;
import com.example.commons.dto.YamlURLsRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class TaskController {

    Logger logger = LoggerFactory.getLogger(ClusterApplierApplication.class);

    @Autowired
    StreamBridge streamBridge;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Bean
    public Consumer<YamlURLsRequest> taskInput() {
        return request -> {
            logger.info("Received request to process: {}", request);
            applicationEventPublisher.publishEvent(new RequestReceivedEvent(request));
        };
    }

    @EventListener
    public void onYamlAppliedEvent(YamlAppliedEvent taskProcessedEvent) {
        logger.info("Sending yaml applying result: {}", taskProcessedEvent.getSource());
        streamBridge.send("yamlApplied", taskProcessedEvent.getSource());
    }

}