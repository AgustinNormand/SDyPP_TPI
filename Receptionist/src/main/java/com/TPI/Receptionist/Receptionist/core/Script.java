package com.TPI.Receptionist.Receptionist.core;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Represents a k8s script that can be applied in the cluster
 */
@Data
public class Script {

    private String filename;

    private byte[] fileBytes;

    public Script(String filename, byte[] fileBytes) {
        this.filename = filename;
        this.fileBytes = fileBytes;
    }
}