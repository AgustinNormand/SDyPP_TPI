package com.example.yamlManager.utils;

import com.example.yamlManager.exceptions.InvalidYamlProcessRequestException;
import com.example.yamlManager.service.yaml.model.YamlValidated;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
public class YamlValidator {

    private Yaml yamlMapper;

    @PostConstruct
    private void setup() {
        DumperOptions options = new DumperOptions();
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        this.yamlMapper = new Yaml(options);
    }

    public YamlValidator() {
        this.setup();
    }

    @Async
    public CompletableFuture<YamlValidated> validateYaml(String jobId, byte[] file) {
        Map<String, Object> yamlValues = yamlMapper.load(new ByteArrayInputStream(file));

        validateDeployment(yamlValues);
        setReplicas(yamlValues);

        String name = setName(jobId, yamlValues);

        String yaml = yamlMapper.dump(yamlValues);

        return CompletableFuture.completedFuture(YamlValidated.of(name, yaml.getBytes(StandardCharsets.UTF_8)));
    }

    private String setName(String jobId, Map<String, Object> yamlValues) {
        try {
            Object metadata = yamlValues.get("metadata");

            Map<String, Object> metadataMap = (Map<String, Object>) metadata;

            String name = String.format("%s-%s", metadataMap.get("name"), jobId);
            metadataMap.put("name", name);

            return name;
        }
        catch (Exception e) {
            throw new InvalidYamlProcessRequestException(String.format("Couldn't set the number of replicas: %s", e.getMessage()));
        }
    }

    private void setReplicas(Map<String, Object> yamlValues) {
        try {
            Object spec = yamlValues.get("spec");
            ((Map<String, Object>) spec).put("replicas", 1);
        }
        catch (Exception e) {
            throw new InvalidYamlProcessRequestException(String.format("Couldn't set the number of replicas: %s", e.getMessage()));
        }
    }

    private void validateDeployment(Map<String, Object> yamlValues) throws InvalidYamlProcessRequestException {
        try {
            Object kind = yamlValues.get("kind");
            String kindString = (String) kind;
            if (Boolean.FALSE.equals(kindString.equals("Deployment"))) {
                throw new InvalidYamlProcessRequestException("The only supported manifest kind is Deployment");
            }
        }
        catch (Exception e) {
            throw new InvalidYamlProcessRequestException(String.format("The given manifest is invalid: %s", e.getMessage()));
        }
    }

}
