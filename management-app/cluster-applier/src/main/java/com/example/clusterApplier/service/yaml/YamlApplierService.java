package com.example.clusterApplier.service.yaml;

import com.example.clusterApplier.core.CoreProcessor;
import com.example.clusterApplier.core.ProcessRequest;
import com.example.clusterApplier.core.ProcessRequestFactory;
import com.example.clusterApplier.exceptions.InvalidYamlProcessRequestException;
import com.example.clusterApplier.service.blob.DownloadService;
import com.example.commons.dto.ProcessResultDto;
import com.example.commons.dto.Task;
import com.example.commons.dto.YamlFilesRequest;
import com.example.commons.dto.YamlProcessResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class YamlApplierService {

    @Autowired
    CoreProcessor coreProcessor;

    @Autowired
    DownloadService downloadService;

    private Logger logger = LoggerFactory.getLogger(YamlApplierService.class);

    public YamlProcessResult apply(Task task) {
        if (Objects.isNull(task)) {
            throw new InvalidYamlProcessRequestException();
        }

        String jobId = task.getJobId();
        YamlProcessResult result = new YamlProcessResult(jobId);

        byte[] blobBytes = downloadService.downloadBlob(task.getBlobName());

        YamlFilesRequest request = (YamlFilesRequest) SerializationUtils.deserialize(blobBytes);

        ProcessRequest processRequest = ProcessRequestFactory
                .createProcessRequest(jobId, request.getSplitter(), request.getWorker(), request.getJoiner(), request.getAutoscaler());

        ProcessResultDto processResult = coreProcessor.apply(processRequest);

        result.setProcessResult(processResult);

        return result;
    }
}
