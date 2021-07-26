package com.example.clusterApplier.core;

import com.example.clusterApplier.core.enums.ResultStatus;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class ProcessResult {

    private ResultStatus status;

    private List<ClusterOperationResult> applied;

    private List<ClusterOperationResult> failed;

    public ProcessResult() {
        this.applied = new ArrayList<>();
        this.failed = new ArrayList<>();
    }

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
