package com.example.clusterApplier.service.task;

import com.example.clusterApplier.events.YamlAppliedEvent;
import com.example.clusterApplier.events.RequestReceivedEvent;
import com.example.clusterApplier.service.yaml.YamlProcessResult;
import com.example.clusterApplier.service.yaml.YamlProcessorService;
import com.example.commons.dto.YamlURLsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class TaskReceptorService {

    @Autowired
    YamlProcessorService processorService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @EventListener
    public void onTaskReceived(RequestReceivedEvent event) {
        YamlURLsRequest request = event.getSource();

        YamlProcessResult yamlProcessResult = processorService.processRequest(request);

        applicationEventPublisher.publishEvent(new YamlAppliedEvent(yamlProcessResult));
    }

}