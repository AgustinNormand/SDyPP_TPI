package com.example.clusterApplier.handlers;

import com.example.clusterApplier.events.YamlAppliedEvent;
import com.example.clusterApplier.events.RequestReceivedEvent;
import com.example.clusterApplier.service.yaml.YamlProcessorService;
import com.example.commons.dto.Task;
import com.example.commons.dto.YamlProcessResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class TaskHandler {

    @Autowired
    YamlProcessorService processorService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @EventListener
    public void onTaskReceived(RequestReceivedEvent event) {
        Task request = event.getSource();

        YamlProcessResult yamlProcessResult = processorService.processRequest(request);

        applicationEventPublisher.publishEvent(new YamlAppliedEvent(yamlProcessResult));
    }

}