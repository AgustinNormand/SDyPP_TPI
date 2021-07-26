package com.example.entrypoint.service;


import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UploadService {

    @Autowired
    Storage storage;

    @Value("${gcp.bucket.name}")
    private String bucketName;

    public String upload(String id, Object payload) {
        Bucket bucket = storage.get(bucketName);
        byte[] data = SerializationUtils.serialize(payload);
        Blob blob = bucket.create(id, data);
        return blob.getMediaLink();
    }
}
