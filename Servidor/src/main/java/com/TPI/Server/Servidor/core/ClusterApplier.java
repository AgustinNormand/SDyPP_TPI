package com.TPI.Server.Servidor.core;

import com.TPI.Server.Servidor.utils.TempFileHelper;
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

    // ToDo: Check that '0' is the right value
    public static final int RIGHT_EXIT_VALUE = 0;

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
        return performClusterOperation("apply", jobId, script);
    }


    /**
     * Rollbacks synchronously the given script from the k8s cluster
     *
     * @param jobId
     * @param script the script to be applied
     * @return a {@link ClusterOperationResult} indicating whether the script could be applied
     */
    protected ClusterOperationResult rollback(UUID jobId, Script script) {
        return performClusterOperation("delete", jobId, script);
    }

    /**
     * Synchronously performs the given operation on the k8s cluster
     * @param operation A {@link String} representing the operation to be applied
     * @param jobId
     * @param script
     * @return A {@link ClusterOperationResult} indicating the state of the operation
     */
    private ClusterOperationResult performClusterOperation(String operation, UUID jobId, Script script) {
        ClusterOperationResult result = new ClusterOperationResult(jobId.toString(), script.getFilename());

        try {
            String pathname = tempFileHelper.save(jobId.toString(), script);
            Process process = Runtime.getRuntime()
                    .exec(String.format("kubectl %s -f %s", operation, pathname))
                    .onExit().get();

            if (process.exitValue() == RIGHT_EXIT_VALUE) {
                result.setOkStatus();
            }
            else {
                result.setErrorStatus();
            }

        } catch (IOException | InterruptedException | ExecutionException e) {
            logger.error("Error performing {} operation script on cluster - {}", operation, e.getMessage());
            logger.debug(Arrays.toString(e.getStackTrace()));
            result.setErrorStatus();
        }

        return result;
    }

}