package com.example.yamlManager.handlers;

import com.example.yamlManager.events.TaskProcessedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TaskProcessedHandler {

    private Logger logger = LoggerFactory.getLogger(TaskProcessedHandler.class);

    @Autowired
    StreamBridge streamBridge;

    @EventListener
    public void taskOutput(TaskProcessedEvent taskProcessedEvent) {
        logger.info("Sending task processed: {}", taskProcessedEvent.getSource());
        streamBridge.send("taskOutput", taskProcessedEvent.getSource());
    }

}
