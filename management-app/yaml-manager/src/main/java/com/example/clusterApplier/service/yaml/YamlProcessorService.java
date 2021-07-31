package com.example.clusterApplier.service.yaml;

import com.example.clusterApplier.exceptions.InvalidYamlProcessRequestException;
import com.example.clusterApplier.service.blob.UploadService;
import com.example.commons.dto.Task;
import com.example.commons.dto.YamlFilesRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class YamlProcessorService {

    public static final String SPLITTER_SUFFIX = "-splitter";
    public static final String WORKER_SUFFIX = "-worker";
    public static final String JOINER_SUFFIX = "-joiner";
    private Logger logger = LoggerFactory.getLogger(YamlProcessorService.class);

    @Autowired
    UploadService uploadService;


    public Task processRequest(String jobId, YamlFilesRequest request) {
        if (Objects.isNull(request)) {
            logger.debug("Yaml request cannot be null");
            throw new InvalidYamlProcessRequestException();
        }

        byte[] splitter = request.getSplitter();
        byte[] worker = request.getWorker();
        byte[] joiner = request.getJoiner();

        logger.debug("Validating yaml files");

        // ToDo: Validate files or update them
        logger.debug("Yaml files validated");

        YamlFilesRequest validated = new YamlFilesRequest(splitter, worker, joiner);

        String blobName = uploadService.upload(jobId, validated);

        return new Task(jobId, blobName);
    }
}