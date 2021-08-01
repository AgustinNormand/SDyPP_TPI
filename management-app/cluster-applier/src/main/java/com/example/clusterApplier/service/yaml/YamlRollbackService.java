package com.example.clusterApplier.service.yaml;

import com.example.clusterApplier.core.CoreProcessor;
import com.example.clusterApplier.core.ProcessRequest;
import com.example.clusterApplier.core.ProcessRequestFactory;
import com.example.clusterApplier.events.YamlRolledBackEvent;
import com.example.clusterApplier.service.blob.DownloadService;
import com.example.commons.dto.ProcessResultDto;
import com.example.commons.dto.Task;
import com.example.commons.dto.YamlFilesRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class YamlRollbackService {

    @Autowired
    private DownloadService downloadService;

    @Autowired
    private CoreProcessor coreProcessor;

    private Logger logger = LoggerFactory.getLogger(YamlRollbackService.class);

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public ProcessResultDto rollback(Task task) {
        byte[] blob = downloadService.downloadBlob(task.getJobId());
        logger.debug("Downloaded blob {}", task.getJobId());

        YamlFilesRequest request = (YamlFilesRequest) SerializationUtils.deserialize(blob);
        logger.debug("YamlFilesRequest deserialized");

        ProcessRequest processRequest = ProcessRequestFactory
                .createProcessRequest(task.getJobId(), request.getSplitter(), request.getWorker(), request.getJoiner());

        ProcessResultDto rollbackResult = coreProcessor.rollback(processRequest);

        eventPublisher.publishEvent(new YamlRolledBackEvent(rollbackResult));

        return rollbackResult;
    }

}