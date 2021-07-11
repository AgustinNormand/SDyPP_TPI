package com.TPI.Server.Servidor.core;

import java.io.InputStream;

/**
 * Represents a k8s script that can be applied in the cluster
 */
public class Script {

    private final String filename;

    private final InputStream file;

    public Script(String originalFilename, InputStream inputStream) {
        this.filename = originalFilename;
        this.file = inputStream;
    }

    public String getFilename() {
        return this.filename;
    }

    public InputStream getInputStream() {
        return this.file;
    }
}
