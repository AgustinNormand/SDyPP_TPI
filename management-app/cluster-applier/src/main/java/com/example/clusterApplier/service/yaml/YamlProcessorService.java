package com.example.clusterApplier.service.yaml;

import com.example.clusterApplier.core.CoreProcessor;
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
public class YamlProcessorService {

    @Autowired
    CoreProcessor coreProcessor;

    @Autowired
    DownloadService downloadService;

    private Logger logger = LoggerFactory.getLogger(YamlProcessorService.class);

    public YamlProcessResult processRequest(Task task) {
        if (Objects.isNull(task)) {
            throw new InvalidYamlProcessRequestException();
        }

        YamlProcessResult result = new YamlProcessResult(task.getJobId());

        byte[] blobBytes = downloadService.downloadBlob(task.getBlobName());

        YamlFilesRequest request = (YamlFilesRequest) SerializationUtils.deserialize(blobBytes);

        ProcessResultDto processResult = coreProcessor
                .process(ProcessRequestFactory.createProcessRequest(request.getSplitter(), request.getWorker(), request.getJoiner()));
        result.setProcessResult(processResult);

        return result;
    }
}
