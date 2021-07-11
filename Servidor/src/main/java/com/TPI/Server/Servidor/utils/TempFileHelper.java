package com.TPI.Server.Servidor.utils;

import com.TPI.Server.Servidor.core.Script;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;

@Component
public class TempFileHelper {

    // ToDo: Check the best dir for uploaded resources
    public static final String BASE_DIR = "src/main/resources/temp";

    public String save(String jobId, Script script) throws IOException {
        String pathname = String.format("%s/%s/%s", BASE_DIR, jobId, script.getFilename());

        FileCopyUtils.copy(script.getInputStream().readAllBytes(), new File(pathname));

        return pathname;
    }
}