package com.example.yamlManager;

import com.example.yamlManager.service.yaml.model.YamlValidated;
import com.example.yamlManager.utils.YamlValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.utils.SerializationUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class YamlValidatorTests {

    private YamlValidator validator;

    @BeforeEach
    public void setup() {
        this.validator = new YamlValidator();
    }

    @Test
    public void yamlValidationTest() throws IOException, ExecutionException, InterruptedException {
        Path path = Path.of("src/test/resources/deployment.yaml");
        byte[] fileBytes = Files.readAllBytes(path);
        String jobId = "example";

        CompletableFuture<YamlValidated> result = validator.validateYaml(jobId, fileBytes);

        byte[] bytes = result.get().getSerializedContent();
        System.out.println("Validation output: " + SerializationUtils.deserialize(bytes));
    }
}