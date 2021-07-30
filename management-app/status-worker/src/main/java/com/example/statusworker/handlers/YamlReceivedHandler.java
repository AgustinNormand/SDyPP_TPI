package com.example.statusworker.handlers;

import com.example.commons.dto.JobStatus;
import com.example.commons.dto.Task;
import com.example.statusworker.service.RedisService;
import com.example.statusworker.events.YamlForValidationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class YamlReceivedHandler {

    @Autowired
    RedisService redis;

    @EventListener
    public void onYamlReceivedEvent(YamlForValidationEvent event) {
        Task task = event.getSource();

        redis.save(task.getJobId(), JobStatus.State.PENDING, task.getBlobName());
    }

}