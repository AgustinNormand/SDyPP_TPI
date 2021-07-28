package com.example.entrypoint.controller;

import com.example.commons.dto.JobResponse;
import com.example.commons.dto.YamlFilesRequest;
import com.example.entrypoint.service.OrchestratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/yaml")
public class YAMLController {

    /**
     * Must match with the destination property in application.properties
     */
    public static final String YAML_VALIDATOR = "yamlValidator";

    Logger logger = LoggerFactory.getLogger(YAMLController.class);

    @Autowired
    OrchestratorService orchestratorService;

    @PostMapping
    public ResponseEntity<JobResponse> processYamlScripts(@RequestBody List<MultipartFile> files) {
        logger.info("Uploaded scripts {}", files.size());

        try {
            YamlFilesRequest yamlFilesRequest =
                    new YamlFilesRequest(files.get(0).getBytes(), files.get(1).getBytes(), files.get(2).getBytes());
            JobResponse result = orchestratorService.process(yamlFilesRequest, YAML_VALIDATOR);

            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch (IOException e){
            logger.error("Couldn't read from uploaded files. {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

}