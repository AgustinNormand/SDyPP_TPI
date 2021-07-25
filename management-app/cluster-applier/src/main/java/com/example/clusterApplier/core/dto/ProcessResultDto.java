package com.example.clusterApplier.core.dto;

import com.example.clusterApplier.core.enums.ResultStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProcessResultDto {

    @JsonProperty("status")
    private ResultStatus status;

    @JsonProperty("applied")
    private List<ClusterOperationResultDto> applied;

    @JsonProperty("failed")
    private List<ClusterOperationResultDto> failed;

}
