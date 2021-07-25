package com.example.clusterApplier.controller;

import com.example.clusterApplier.YAMLManagerApplication;
import com.example.clusterApplier.events.TaskProcessedEvent;
import com.example.clusterApplier.events.TaskReceivedEvent;
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

    Logger logger = LoggerFactory.getLogger(YAMLManagerApplication.class);

    @Autowired
    StreamBridge streamBridge;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Bean
    public Consumer<Task> taskInput() {
        return task -> {
            logger.info("Received task to process: {}", task);

            applicationEventPublisher.publishEvent(new TaskReceivedEvent(task));
        };
    }

    @EventListener
    public void taskOutput(TaskProcessedEvent taskProcessedEvent) {
        logger.info("Sending task processed: {}", taskProcessedEvent.getSource());
        streamBridge.send("taskOutput", taskProcessedEvent.getSource());
    }

}