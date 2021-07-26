package com.example.clusterApplier.service.notifier;

import com.example.commons.dto.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
public class JobNotifierService {

    @Autowired
    StreamBridge streamBridge;

    public void yamlValidated(String outputQueue, Task task) {
        streamBridge.send(outputQueue, task);
    }
}
