package com.example.clusterApplier.service.blob;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UploadService {

    private Logger logger = LoggerFactory.getLogger(UploadService.class);

    @Autowired
    Storage storage;

    @Value("${gcp.bucket.name}")
    private String bucketName;

    public String upload(String id, Object payload) {
        Bucket bucket = storage.get(bucketName);
        byte[] data = SerializationUtils.serialize(payload);
        Blob blob = bucket.create(id, data);
        logger.debug("Uploaded payload {} to bucket {} returning {}", id, bucket.getName(), blob.getName());
        return blob.getName();
    }
}