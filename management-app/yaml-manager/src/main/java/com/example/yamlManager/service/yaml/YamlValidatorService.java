package com.example.yamlManager.service.yaml;

import com.example.commons.dto.YamlFilesRequest;
import com.example.yamlManager.service.yaml.model.ValidatedYamlFilesRequest;
import com.example.yamlManager.service.yaml.model.YamlValidated;
import com.example.yamlManager.utils.YamlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class YamlValidatorService {

    Logger logger = LoggerFactory.getLogger(YamlValidatorService.class);

    @Autowired
    YamlValidator yamlValidator;

    public ValidatedYamlFilesRequest validateYamlRequest(String jobId, YamlFilesRequest request) {
        logger.debug("Validating yaml files");

        CompletableFuture<YamlValidated> joinerFuture = yamlValidator.validateYaml(jobId, request.getJoiner());
        CompletableFuture<YamlValidated> splitterFuture = yamlValidator.validateYaml(jobId, request.getSplitter());
        CompletableFuture<YamlValidated> workerFuture = yamlValidator.validateYaml(jobId, request.getWorker());

        logger.debug("Yaml files validated");
        try {
            return new ValidatedYamlFilesRequest(splitterFuture.get(), joinerFuture.get(), workerFuture.get());
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Interrupted exception: {}", e.getMessage());
            return ValidatedYamlFilesRequest.empty();
        }
    }

}