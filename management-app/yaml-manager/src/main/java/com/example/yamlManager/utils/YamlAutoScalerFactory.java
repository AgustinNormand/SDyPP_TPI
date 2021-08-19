package com.example.yamlManager.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class YamlAutoScalerFactory {

    private Logger logger = LoggerFactory.getLogger(YamlAutoScalerFactory.class);

    private Yaml yamlMapper;

    private final String TEMPLATE_VALUE = "<<deployment_name>>";

    @PostConstruct
    private void setup() {
        DumperOptions options = new DumperOptions();
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        this.yamlMapper = new Yaml(options);
    }

    public byte[] createAutoScaler(String resourceName) {
        try {
            logger.debug("Creating autoscaler");

            String template = Files.readString(Path.of("classpath:autoscaler.yaml"));

            logger.debug("Template loaded: {}", template);

            String finalYamlString = template.replace(TEMPLATE_VALUE, resourceName);

            logger.debug("Autoscaler created: {}", finalYamlString);

            return finalYamlString.getBytes(StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.error("Couldn't create autoscaler: {} - {}", e.getMessage(), e.getLocalizedMessage());
            e.printStackTrace();
            return new byte[0];
        }
    }
}
