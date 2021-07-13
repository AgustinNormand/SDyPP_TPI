package com.TPI.Receptionist.Receptionist.core;

import com.TPI.Receptionist.Receptionist.utils.ClusterOpResultMapper;
import com.TPI.Receptionist.Receptionist.utils.TempFileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Component
public class ClusterApplier {

    public static final int OK_EXIT_VALUE = 0;

    Logger logger = LoggerFactory.getLogger(ClusterApplier.class);

    @Autowired
    TempFileHelper tempFileHelper;

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

            Process process = Runtime.getRuntime()
                    .exec(commandToExecute)
                    .onExit().get();

            result.setResultContent(ClusterOpResultMapper.getProcessResult(process));

            if (process.exitValue() == OK_EXIT_VALUE) {
                result.setOkStatus();
            } else {
                result.setErrorStatus();
            }

        } catch (IOException | InterruptedException | ExecutionException e) {
            logger.error("Error performing '{}' on cluster - {}", command, e.getMessage());
            logger.debug(Arrays.toString(e.getStackTrace()));
            result.setErrorStatus();
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
        return performScriptOperationOnCluster("delete", jobId, script);
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

            result = this.execute(jobId, String.format("%s -f %s", operation, pathname));

        } catch (IOException e) {
            logger.error("Error performing {} operation script on cluster - {}", operation, e.getMessage());
            logger.debug(Arrays.toString(e.getStackTrace()));
            result.setErrorStatus();
        }

        return result;
    }

}