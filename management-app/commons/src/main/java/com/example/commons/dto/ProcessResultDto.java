package com.example.commons.dto;

import com.example.commons.dto.enums.ResultStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class ProcessResultDto {

    @JsonProperty("status")
    private ResultStatus status;

    @JsonProperty("applied")
    private List<ClusterOperationResultDto> applied;

    @JsonProperty("failed")
    private List<ClusterOperationResultDto> failed;

}
