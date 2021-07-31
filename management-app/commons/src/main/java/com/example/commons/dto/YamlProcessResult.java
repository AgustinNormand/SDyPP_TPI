package com.example.commons.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class YamlProcessResult {

    @JsonProperty("job_id")
    private String jobId;

    @JsonProperty("invalid_files")
    private List<String> invalidFileUrls;

    @JsonProperty("process_result")
    private ProcessResultDto processResult;

    public YamlProcessResult(String jobId) {
        this.jobId = jobId;
    }

    public void setInvalidFileUrls(List<String> invalidFileUrls) {
        this.invalidFileUrls = invalidFileUrls;
    }

    public void setProcessResult(ProcessResultDto processResult) {
        this.processResult = processResult;
    }
}
