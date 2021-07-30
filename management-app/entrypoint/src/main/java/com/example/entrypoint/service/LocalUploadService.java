package com.example.entrypoint.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class LocalUploadService {

    Logger logger = LoggerFactory.getLogger(LocalUploadService.class);

    public String upload(String id, Object payload) {
        try {
            Path tempFile = Files.createTempFile(null, null);
            Files.write(tempFile, SerializationUtils.serialize(payload));
            logger.debug("Uploaded payload {} to local path {}", id, tempFile.getFileName());
            return tempFile.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
