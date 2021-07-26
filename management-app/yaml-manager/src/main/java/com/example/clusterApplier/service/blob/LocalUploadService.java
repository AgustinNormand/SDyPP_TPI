package com.example.clusterApplier.service.blob;


import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class LocalUploadService {


    public String upload(String id, Object payload) {

        try {
            Path tempFile = Files.createTempFile(null, null);
            Files.write(tempFile, SerializationUtils.serialize(payload));
            return tempFile.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
