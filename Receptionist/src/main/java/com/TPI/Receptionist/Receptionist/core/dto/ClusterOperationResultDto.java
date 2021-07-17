package com.TPI.Receptionist.Receptionist.core.dto;

import com.TPI.Receptionist.Receptionist.core.enums.ResultStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClusterOperationResultDto {

    @JsonProperty("job_id")
    private String jobId;

    @JsonProperty("status")
    private ResultStatus status;

    @JsonProperty("resource")
    private String resource;

    @JsonProperty("result_content")
    private String resultContent;

}
