package com.TPI.Server.Servidor.service.yaml;

import com.TPI.Server.Servidor.core.ProcessResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class YamlProcessResult {

    private List<MultipartFile> invalidFiles;

    private ProcessResult processResult;

    public void setInvalidFiles(List<MultipartFile> invalidFiles) {
        this.invalidFiles = invalidFiles;
    }

    public void setProcessResult(ProcessResult processResult) {
        this.processResult = processResult;
    }
}
