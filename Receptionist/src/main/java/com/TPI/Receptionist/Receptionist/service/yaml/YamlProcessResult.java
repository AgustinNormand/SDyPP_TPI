package com.TPI.Receptionist.Receptionist.service.yaml;

import com.TPI.Receptionist.Receptionist.core.ProcessResult;
import com.TPI.Receptionist.Receptionist.core.dto.ProcessResultDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class YamlProcessResult {

    @JsonProperty("invalid_files")
    private List<MultipartFile> invalidFiles;

    @JsonProperty("process_result")
    private ProcessResultDto processResult;

    public void setInvalidFiles(List<MultipartFile> invalidFiles) {
        this.invalidFiles = invalidFiles;
    }

    public void setProcessResult(ProcessResultDto processResult) {
        this.processResult = processResult;
    }
}
