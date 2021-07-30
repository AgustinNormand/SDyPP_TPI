package com.example.entrypoint.service;

import com.example.commons.dto.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
public class JobNotifierService {

    @Autowired
    StreamBridge streamBridge;

    Logger logger = LoggerFactory.getLogger(JobNotifierService.class);

    public void jobCreated(String outputQueue, Task task) {
        streamBridge.send(outputQueue, task);
        logger.debug("Sent task {} to queue {}", task, outputQueue);
    }
}
