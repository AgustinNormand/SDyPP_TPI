package com.example.clusterApplier.core;

import com.example.clusterApplier.core.dto.mappers.ProcessResultMapper;
import com.example.clusterApplier.core.exceptions.InvalidProcessRequestException;
import com.example.commons.dto.ProcessResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class CoreProcessor {

    @Autowired
    private ClusterApplier clusterApplier;

    /**
     * Applies the given request. Creates the required resources in the cluster and rollbacks all of them if there's
     * any error
     * @param processRequest the processing request
     * @return A {@link ProcessResult} indicating the status of the resources
     */
    public ProcessResultDto apply(ProcessRequest processRequest) throws InvalidProcessRequestException {
        return ProcessResultMapper.map(this.applyRequest(processRequest));
    }

    /**
     * Applies the given request. Creates the required resources in the cluster and rollbacks all of them if there's
     * any error
     * @param processRequest the processing request
     * @return A {@link ProcessResult} indicating the status of the resources
     */
    public ProcessResultDto rollback(ProcessRequest processRequest) throws InvalidProcessRequestException {
        return ProcessResultMapper.map(this.rollbackRequest(processRequest));
    }


    private ProcessResult applyRequest(ProcessRequest processRequest) throws InvalidProcessRequestException {
        preValidate(processRequest);

        String jobId = processRequest.getJobId();

        ProcessResult processResult = new ProcessResult();

        List<Script> pending = List.copyOf(processRequest.getPending());

        for (Script script : pending) {
            ClusterOperationResult opResult = clusterApplier.apply(jobId, script);
            processResult.addOperationResult(opResult);
            if (!opResult.isOk()) {
                this.rollback(jobId, processRequest.getDone().toArray(new Script[0]));
                break;
            }
            processRequest.scriptProcessed(script);
        }

        return processResult;
    }


    private void preValidate(ProcessRequest processRequest) {
        if (!processRequest.isValid()) {
            throw new InvalidProcessRequestException(processRequest);
        }
    }


    private ProcessResult rollbackRequest(ProcessRequest processRequest) {
        return rollback(processRequest.getJobId(), processRequest.getPending().toArray(new Script[0]));
    }


    private ProcessResult rollback(String jobId, Script ...scripts) {
        ClusterOperationResult[] rollbackResults = (ClusterOperationResult[]) Arrays.stream(scripts)
                .map(script -> clusterApplier.rollback(jobId, script)).toArray();
        return ProcessResult.of(rollbackResults);
    }

}