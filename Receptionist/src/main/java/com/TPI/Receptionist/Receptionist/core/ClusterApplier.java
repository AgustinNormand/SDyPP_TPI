package com.TPI.Receptionist.Receptionist.core;

import com.TPI.Receptionist.Receptionist.core.enums.ResultStatus;
import com.TPI.Receptionist.Receptionist.utils.ClusterOpResultReader;
import com.TPI.Receptionist.Receptionist.utils.TempFileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private TempFileHelper tempFileHelper;

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
    protected ClusterOperationResult apply(UUID jobId, Script script) {
        return performScriptOperationOnCluster("apply", jobId, script);
    }

    /**
     * Applies synchronously the given command in the k8s cluster
     *
     * @param jobId
     * @param command the command to be applied
     * @return a {@link ClusterOperationResult} indicating whether the command could be executed
     */
    protected ClusterOperationResult execute(UUID jobId, String command) {
        ClusterOperationResult result = new ClusterOperationResult(jobId.toString(), command);

        try {

            String commandToExecute = String.format("kubectl %s", command);
            logger.debug("Executing '{}' in cluster", commandToExecute);

            if (!deactivateApplier) {

                Process process = Runtime.getRuntime()
                        .exec(commandToExecute)
                        .onExit().get();

                if (process.exitValue() == OK_EXIT_VALUE) {
                    result.setResultContent(ClusterOpResultReader.getProcessResult(process));
                    result.setStatus(ResultStatus.OK);
                } else {
                    result.setResultContent(ClusterOpResultReader.getProcessError(process));
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


    /**
     * Rollbacks synchronously the given script from the k8s cluster
     *
     * @param jobId
     * @param script the script to be applied
     * @return a {@link ClusterOperationResult} indicating whether the script could be applied
     */
    protected ClusterOperationResult rollback(UUID jobId, Script script) {
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
    private ClusterOperationResult performScriptOperationOnCluster(String operation, UUID jobId, Script script) {
       
        ClusterOperationResult result = new ClusterOperationResult(jobId.toString(), script.getFilename());

        try {
            String pathname = tempFileHelper.save(jobId.toString(), script);

            ClusterOperationResult clusterExecResult = this.execute(jobId, String.format("%s -f %s", operation, pathname));
            result.setResultContent(clusterExecResult.getResultContent());
            result.setStatus(clusterExecResult.getStatus());

        } catch (IOException e) {
            logger.error("Error performing {} operation script on cluster - {}", operation, e.getMessage());
            logger.debug(Arrays.toString(e.getStackTrace()));
            result.setStatus(ResultStatus.ERROR);
        }

        return result;
    }

}