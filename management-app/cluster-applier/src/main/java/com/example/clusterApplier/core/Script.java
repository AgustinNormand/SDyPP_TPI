package com.example.clusterApplier.core;

import com.example.clusterApplier.utils.TempFileCreator;
import lombok.Getter;

/**
 * Represents a k8s script that can be applied in the cluster
 */
public class Script {

    private String filename;

    @Getter
    private byte[] content;

    private boolean tempFileCreated;

    public Script(byte[] content) {
        this.content = content;
        this.tempFileCreated = false;
    }

    public String getFilename() {
        if (!tempFileCreated) {
            this.filename = TempFileCreator.createTempFile(this.content);
            tempFileCreated = true;
        }
        return filename;
    }
}