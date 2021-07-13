package com.TPI.Receptionist.Receptionist.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SimpleCommandProcessor {

    @Autowired
    ClusterApplier clusterApplier;

    public ClusterOperationResult executeCommand(String command) {
        return clusterApplier.execute(UUID.randomUUID(), command);
    }

    public <T> T executeAndMapResponse(String command, ClusterResponseMapper<T> mapper) {
        ClusterOperationResult result = clusterApplier.execute(UUID.randomUUID(), command);

        return mapper.mapResult(result.getResultContent());
    }


}