package com.example.clusterApplier.service.blob;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DownloadService {

    @Autowired
    Storage storage;

    @Value("${gcp.bucket.name}")
    private String bucketName;

    public byte[] downloadBlob(String blobName) {

        Bucket bucket = storage.get(bucketName);

        Blob blob = bucket.get(blobName);
        return blob.getContent();
    }
}
