package com.example.clusterApplier.core;

import com.example.clusterApplier.core.utils.ClusterOpResultReader;
import com.example.commons.dto.enums.ResultStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Component
public class ClusterApplier {

    public static final int OK_EXIT_VALUE = 0;

    private Logger logger = LoggerFactory.getLogger(ClusterApplier.class);


    /**
     * Flag to stop the applier from actually applying a script in the cluster, for testing purposes
     */
    @Value("${cluster.applier.script.deactivate:false}")
    private boolean deactivateApplier;

    /**
     * Applies synchronously the given script to the k8s cluster
     *
     * @param jobId
     * @param script the script to be applied
     * @return a {@link ClusterOperationResult} indicating whether the script could be applied
     */
    protected ClusterOperationResult apply(String jobId, Script script) {
        return performScriptOperationOnCluster("apply", jobId, script);
    }



    /**
     * Rollbacks synchronously the given script from the k8s cluster
     *
     * @param jobId
     * @param script the script to be applied
     * @return a {@link ClusterOperationResult} indicating whether the script could be applied
     */
    protected ClusterOperationResult rollback(String jobId, Script script) {
        ClusterOperationResult result = performScriptOperationOnCluster("delete", jobId, script);
        result.setRolledBack(true);
        return result;
    }

    /**
     * Synchronously performs the given operation on the k8s cluster
     * @param operation A {@link String} representing the operation to be applied
     * @param jobId
     * @param script
     * @return A {@link ClusterOperationResult} indicating the state of the operation
     */
    private ClusterOperationResult performScriptOperationOnCluster(String operation, String jobId, Script script) {

        ClusterOperationResult result = new ClusterOperationResult(jobId, script.getFilename());

        ClusterOperationResult clusterExecResult = this.execute(jobId, String.format("%s -f %s", operation, script.getFilename()));
        result.setResultContent(clusterExecResult.getResultContent());
        result.setStatus(clusterExecResult.getStatus());

        return result;
    }

    /**
     * Applies synchronously the given command in the k8s cluster
     *
     * @param jobId
     * @param command the command to be applied
     * @return a {@link ClusterOperationResult} indicating whether the command could be executed
     */
    protected ClusterOperationResult execute(String jobId, String command) {
        ClusterOperationResult result = new ClusterOperationResult(jobId, command);

        try {

            String commandToExecute = String.format("kubectl %s", command);
            logger.debug("Executing '{}' in cluster", commandToExecute);

            if (Boolean.FALSE.equals(deactivateApplier)) {

                Process process = Runtime.getRuntime()
                        .exec(commandToExecute)
                        .onExit().get();

                if (process.exitValue() == OK_EXIT_VALUE) {
                    logger.debug("Command {} executed successfully", commandToExecute);
                    result.setResultContent(ClusterOpResultReader.getProcessResult(process));
                    result.setStatus(ResultStatus.OK);
                } else {
                    String processError = ClusterOpResultReader.getProcessError(process);
                    logger.debug("Command {} returned an error {}", commandToExecute, processError);
                    result.setResultContent(processError);
                    result.setStatus(ResultStatus.ERROR);
                }

            }
            else {
                logger.debug("Cluster execution is deactivated. Activate it by removing the cluster.applier.script.deactivate property");
                result.setStatus(ResultStatus.OK);
            }
        } catch (IOException | InterruptedException | ExecutionException e) {
            logger.error("Error performing '{}' on cluster - {}", command, e.getMessage());
            logger.debug(Arrays.toString(e.getStackTrace()));
            result.setStatus(ResultStatus.ERROR);
        }

        return result;
    }

}