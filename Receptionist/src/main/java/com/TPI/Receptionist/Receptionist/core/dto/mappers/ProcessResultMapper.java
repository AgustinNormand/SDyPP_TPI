package com.TPI.Receptionist.Receptionist.core.dto.mappers;

import com.TPI.Receptionist.Receptionist.core.ProcessResult;
import com.TPI.Receptionist.Receptionist.core.dto.ProcessResultDto;

import java.util.stream.Collectors;

public class ProcessResultMapper {
    public static ProcessResultDto map(ProcessResult processRequest) {
        ProcessResultDto response = new ProcessResultDto();
        response.setApplied(processRequest.getApplied().stream().map(ClusterOperationMapper::map).collect(Collectors.toList()));
        response.setFailed(processRequest.getFailed().stream().map(ClusterOperationMapper::map).collect(Collectors.toList()));
        response.setStatus(processRequest.getStatus());
        return response;
    }
}
