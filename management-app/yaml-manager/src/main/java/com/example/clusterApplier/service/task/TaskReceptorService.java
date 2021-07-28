package com.example.clusterApplier.service.task;

import com.example.clusterApplier.events.TaskProcessedEvent;
import com.example.clusterApplier.events.TaskReceivedEvent;
import com.example.clusterApplier.service.blob.DownloadService;
import com.example.clusterApplier.service.yaml.YamlProcessorService;
import com.example.commons.dto.Task;
import com.example.commons.dto.YamlFilesRequest;
import com.example.commons.dto.YamlURLsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;

@Service
public class TaskReceptorService {

    @Autowired
    DownloadService downloadService;

    @Autowired
    YamlProcessorService processorService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @EventListener
    public void onTaskReceived(TaskReceivedEvent event) {
        Task task = event.getSource();

        byte[] blobBytes = downloadService.downloadBlob(task.getBlobName());

        YamlFilesRequest request = (YamlFilesRequest) SerializationUtils.deserialize(blobBytes);

        Task processedTask = processorService.processRequest(task.getJobId(), request);

        applicationEventPublisher.publishEvent(new TaskProcessedEvent(processedTask));
    }

}