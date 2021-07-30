package com.example.statusworker.handlers;

import com.example.commons.dto.JobStatus;
import com.example.commons.dto.Task;
import com.example.statusworker.service.RedisService;
import com.example.statusworker.events.YamlValidatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class YamlValidatedHandler {

    @Autowired
    RedisService redis;

    @EventListener
    public void onYamlValidatedEvent(YamlValidatedEvent event) {
        Task task = event.getSource();

        redis.save(task.getJobId(), JobStatus.State.APPLYING_IN_CLUSTER, task.getBlobName());
    }

}