package com.example.clusterApplier.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TempFileCreator {

    public static String createTempFile(byte[] payload) {
        try {
            Path tempFile = Files.createTempFile(null, null);
            Files.write(tempFile, payload);
            return tempFile.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
