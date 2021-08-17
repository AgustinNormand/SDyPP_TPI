package com.example.yamlManager.service.blob;

import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class LocalDownloadService {

    @Autowired
    Storage storage;

    @Value("${gcp.bucket.name}")
    private String bucketName;

    public byte[] downloadBlob(String pathname) {
        try {
            return Files.readAllBytes(Path.of(pathname));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
