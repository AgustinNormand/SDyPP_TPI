package com.example.clusterApplier.handlers;

import com.example.clusterApplier.events.YamlAppliedEvent;
import com.example.clusterApplier.events.ApplyTaskReceivedEvent;
import com.example.clusterApplier.service.yaml.YamlApplierService;
import com.example.commons.dto.Task;
import com.example.commons.dto.YamlProcessResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ApplyTaskHandler {

    @Autowired
    YamlApplierService processorService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @EventListener
    public void onTaskReceived(ApplyTaskReceivedEvent event) {
        Task request = event.getSource();

        YamlProcessResult yamlProcessResult = processorService.apply(request);

        applicationEventPublisher.publishEvent(new YamlAppliedEvent(yamlProcessResult));
    }

}