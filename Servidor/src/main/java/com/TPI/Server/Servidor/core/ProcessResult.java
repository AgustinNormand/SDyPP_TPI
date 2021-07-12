package com.TPI.Server.Servidor.core;

import com.TPI.Server.Servidor.core.enums.ResultStatus;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class ProcessResult {

    private ResultStatus status;

    private List<ClusterOperationResult> applied;

    private List<ClusterOperationResult> failed;

    /**
     * Builds a {@link ProcessResult} from the given {@link ClusterOperationResult} objects, classifying each as an applied
     * or failed one
     * @param results
     * @return
     */
    public static ProcessResult of(ClusterOperationResult...results) {
        ProcessResult result = new ProcessResult();
        Arrays.stream(results).forEach(result::addOperationResult);
        result.updateStatus();
        return result;
    }

    private void updateStatus() {
        this.status = this.failed.isEmpty() ? ResultStatus.OK : ResultStatus.ERROR;
    }

    /**
     * Adds the given result to the corresponding list based on its state
     * @param applyResult
     */
    private void addOperationResult(ClusterOperationResult applyResult) {
        if (applyResult.isOk()) {
            this.applied.add(applyResult);
        } else {
            this.failed.add(applyResult);
        }
    }
}
