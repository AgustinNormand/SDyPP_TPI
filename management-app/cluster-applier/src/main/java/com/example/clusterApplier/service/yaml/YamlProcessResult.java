package com.example.clusterApplier.service.yaml;

import com.example.clusterApplier.core.dto.ProcessResultDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class YamlProcessResult {

    @JsonProperty("invalid_files")
    private List<String> invalidFileUrls;

    @JsonProperty("process_result")
    private ProcessResultDto processResult;

    public void setInvalidFileUrls(List<String> invalidFileUrls) {
        this.invalidFileUrls = invalidFileUrls;
    }

    public void setProcessResult(ProcessResultDto processResult) {
        this.processResult = processResult;
    }
}
