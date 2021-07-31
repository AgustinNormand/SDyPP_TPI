package com.example.clusterApplier.core;

import com.example.clusterApplier.core.dto.mappers.ProcessResultMapper;
import com.example.clusterApplier.core.exceptions.InvalidProcessRequestException;
import com.example.commons.dto.ProcessResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

        Script worker = processRequest.getWorker();
        ClusterOperationResult workerApplyResult = clusterApplier.apply(jobId, worker);

        if (!workerApplyResult.isOk()) {
            rollback(jobId, worker);
            return ProcessResult.of(workerApplyResult);
        }

        Script joiner = processRequest.getJoiner();
        ClusterOperationResult joinerApplyResult = clusterApplier.apply(jobId, joiner);

        if (!joinerApplyResult.isOk()) {
            rollback(jobId, worker, joiner);
            return ProcessResult.of(workerApplyResult, joinerApplyResult);
        }

        Script splitter = processRequest.getSplitter();
        ClusterOperationResult splitterApplyResult = clusterApplier.apply(jobId, splitter);

        if (!splitterApplyResult.isOk()) {
            rollback(jobId, worker, joiner, splitter);
        }

        return ProcessResult.of(workerApplyResult, joinerApplyResult, splitterApplyResult);
    }


    private void preValidate(ProcessRequest processRequest) {
        if (!processRequest.isValid()) {
            throw new InvalidProcessRequestException(processRequest);
        }
    }


    private ProcessResult rollbackRequest(ProcessRequest processRequest) {
        return rollback(processRequest.getJobId(), processRequest.getScripts());
    }


    private ProcessResult rollback(String jobId, Script ...scripts) {
        ClusterOperationResult[] rollbackResults = (ClusterOperationResult[]) Arrays.stream(scripts)
                .map(script -> clusterApplier.rollback(jobId, script)).toArray();
        return ProcessResult.of(rollbackResults);
    }

}