package com.example.clusterApplier.controller;

import com.example.clusterApplier.ClusterApplierApplication;
import com.example.clusterApplier.events.RollbackTaskReceivedEvent;
import com.example.clusterApplier.events.YamlAppliedEvent;
import com.example.clusterApplier.events.ApplyTaskReceivedEvent;
import com.example.commons.dto.Task;
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
    public Consumer<Task> applyTask() {
        return request -> {
            logger.info("Received task to apply: {}", request);
            applicationEventPublisher.publishEvent(new ApplyTaskReceivedEvent(request));
        };
    }

    @Bean
    public Consumer<Task> rollbackTask() {
        return request -> {
            logger.info("Received request to rollback: {}", request);
            applicationEventPublisher.publishEvent(new RollbackTaskReceivedEvent(request));
        };
    }

    @EventListener
    public void onYamlAppliedEvent(YamlAppliedEvent taskProcessedEvent) {
        logger.info("Sending yaml applying result: {}", taskProcessedEvent.getSource());
        streamBridge.send("yamlApplied", taskProcessedEvent.getSource());
    }

}