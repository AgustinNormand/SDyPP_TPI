package com.example.clusterApplier.core.dto.mappers;


import com.example.clusterApplier.core.ClusterOperationResult;
import com.example.clusterApplier.core.dto.ClusterOperationResultDto;

public class ClusterOperationMapper {
    public static ClusterOperationResultDto map(ClusterOperationResult result) {
        ClusterOperationResultDto response = new ClusterOperationResultDto();
        response.setResource(result.getResource());
        response.setJobId(result.getJobId());
        response.setResultContent(result.getResultContent());
        response.setStatus(result.getStatus());
        response.setRolledBack(response.isRolledBack());
        return response;
    }
}
