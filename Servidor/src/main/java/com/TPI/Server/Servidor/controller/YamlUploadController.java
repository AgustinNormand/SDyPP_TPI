package com.TPI.Server.Servidor.controller;

import com.TPI.Server.Servidor.service.yaml.YamlProcessResult;
import com.TPI.Server.Servidor.service.ProcessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class YamlUploadController {

    Logger logger = LoggerFactory.getLogger(YamlUploadController.class);

    @Autowired
    private ProcessorService yamlProcessorService;

    @PostMapping("/scripts")
    public ResponseEntity<YamlProcessResult> processYamlScripts(@RequestBody List<MultipartFile> files) {
        logger.debug("Uploaded scripts {}", files.size());

        YamlProcessResult yamlProcessResult = yamlProcessorService.processRequest(files);

        return new ResponseEntity<>(yamlProcessResult, HttpStatus.OK);
    }

}
