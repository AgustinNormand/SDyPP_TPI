package com.example.yamlManager.service.blob;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DownloadService {

    @Autowired
    Storage storage;

    @Value("${gcp.bucket.name}")
    private String bucketName;

    private Logger logger = LoggerFactory.getLogger(DownloadService.class);

    public byte[] downloadBlob(String blobName) {
        Bucket bucket = storage.get(bucketName);
        Blob blob = bucket.get(blobName);

        logger.debug("Downloaded blob {} from bucket {}", blob.getName(), bucket.getName());
        return blob.getContent();
    }
}
