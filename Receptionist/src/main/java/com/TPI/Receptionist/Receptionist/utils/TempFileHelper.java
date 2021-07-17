package com.TPI.Receptionist.Receptionist.utils;

import com.TPI.Receptionist.Receptionist.core.Script;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Component
public class TempFileHelper {

    // ToDo: Check the best dir for uploaded resources
    public static final String BASE_DIR = "temp";
    private Logger logger = LoggerFactory.getLogger(TempFileHelper.class);

    public String save(String jobId, Script script) throws IOException {
        String dirPath = String.format("%s/%s", BASE_DIR, jobId);

        String pathname = String.format("%s/%s", dirPath, script.getFilename());

        File dir = new File(dirPath);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                logger.error("Couldn't create directory - {}", dir.getAbsolutePath());
            }
            else {
                logger.debug("Dir created {}", dir.getAbsolutePath());
            }
        }

        File outputFile = new File(pathname);

        try (OutputStream os = new FileOutputStream(outputFile)) {
            os.write(script.getFileBytes());
        }

        return outputFile.getAbsolutePath();
    }
}