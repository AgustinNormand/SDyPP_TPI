package com.example.clusterApplier.service.yaml;

import com.example.clusterApplier.exceptions.InvalidYamlProcessRequestException;
import com.example.clusterApplier.service.blob.LocalUploadService;
import com.example.clusterApplier.service.notifier.JobNotifierService;
import com.example.commons.dto.YamlFilesRequest;
import com.example.commons.dto.YamlURLsRequest;
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
    LocalUploadService uploadService;

    @Autowired
    JobNotifierService jobNotifierService;

    public YamlURLsRequest processRequest(String jobId, YamlFilesRequest request) {
        if (Objects.isNull(request)) {
            throw new InvalidYamlProcessRequestException();
        }

        byte[] splitter = request.getSplitter();
        byte[] worker = request.getWorker();
        byte[] joiner = request.getJoiner();

        // ToDo: Validate files or update them

        String splitterUrl = uploadService.upload(jobId + SPLITTER_SUFFIX, splitter);
        String workerUrl = uploadService.upload(jobId + WORKER_SUFFIX, worker);
        String joinerUrl = uploadService.upload(jobId + JOINER_SUFFIX, joiner);

        return new YamlURLsRequest(jobId, splitterUrl, workerUrl, joinerUrl);
    }
}