package com.TPI.Receptionist.Receptionist.core.dto.mappers;

import com.TPI.Receptionist.Receptionist.core.ClusterOperationResult;
import com.TPI.Receptionist.Receptionist.core.dto.ClusterOperationResultDto;

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
