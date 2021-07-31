package com.example.clusterApplier.handlers;

import com.example.clusterApplier.events.RollbackTaskReceivedEvent;
import com.example.clusterApplier.service.yaml.YamlRollbackService;
import com.example.commons.dto.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class RollbackTaskHandler {

    @Autowired
    YamlRollbackService service;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @EventListener
    public void onTaskReceived(RollbackTaskReceivedEvent event) {
        Task task = event.getSource();

        Object result = service.rollback(task);

        eventPublisher.publishEvent(result);
    }


}
