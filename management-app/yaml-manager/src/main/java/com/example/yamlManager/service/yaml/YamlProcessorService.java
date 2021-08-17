package com.example.yamlManager.service.yaml;

import com.example.yamlManager.exceptions.InvalidYamlProcessRequestException;
import com.example.yamlManager.service.blob.UploadService;
import com.example.commons.dto.Task;
import com.example.commons.dto.YamlFilesRequest;
import com.example.yamlManager.service.yaml.model.ValidatedYamlFilesRequest;
import com.example.yamlManager.utils.YamlAutoScalerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class YamlProcessorService {

    private Logger logger = LoggerFactory.getLogger(YamlProcessorService.class);

    @Autowired
    UploadService uploadService;

    @Autowired
    YamlValidatorService yamlValidator;

    @Autowired
    YamlAutoScalerFactory autoScalerFactory;

    public Task processRequest(String jobId, YamlFilesRequest request) {
        if (Objects.isNull(request)) {
            logger.debug("Yaml request cannot be null");
            throw new InvalidYamlProcessRequestException();
        }

        ValidatedYamlFilesRequest validated = yamlValidator.validateYamlRequest(jobId, request);

        byte[] autoscaler = autoScalerFactory.createAutoScaler(validated.getWorkerName());

        validated.setAutoscaler(autoscaler);

        String blobName = uploadService.upload(jobId, validated.yamlFilesRequest());

        return new Task(jobId, blobName);
    }
}