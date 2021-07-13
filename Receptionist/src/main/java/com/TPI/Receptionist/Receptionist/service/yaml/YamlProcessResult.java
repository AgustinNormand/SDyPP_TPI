package com.TPI.Receptionist.Receptionist.service.yaml;

import com.TPI.Receptionist.Receptionist.core.ProcessResult;
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
