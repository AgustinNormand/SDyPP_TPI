package com.TPI.Server.Servidor.core;

import com.TPI.Server.Servidor.exceptions.InvalidProcessRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.UUID;

@Component
public class CoreProcessor {

    @Autowired
    private ClusterApplier clusterApplier;

    /**
     * Processes the given request. Creates the required resources in the cluster and rollbacks all of them if there's
     * at least an error in one of them
     * @param processRequest the processing request
     * @return A {@link ProcessResult} indicating the status of the resources
     */
    public ProcessResult process(ProcessRequest processRequest) throws InvalidProcessRequestException {
        preValidate(processRequest);

        UUID jobId = UUID.randomUUID();

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
            return ProcessResult.of(workerApplyResult, joinerApplyResult, splitterApplyResult);
        }

        return ProcessResult.of(workerApplyResult, joinerApplyResult, splitterApplyResult);
    }

    private void preValidate(ProcessRequest processRequest) {
        if (!processRequest.isValid()) {
            throw new InvalidProcessRequestException(processRequest);
        }
    }

    private void rollback(UUID jobId, Script ...scripts) {
        Arrays.stream(scripts).forEach(script -> clusterApplier.rollback(jobId, script));
    }

}
