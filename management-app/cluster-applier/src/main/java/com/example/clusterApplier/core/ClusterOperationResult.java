package com.example.clusterApplier.core;

import com.example.clusterApplier.core.enums.ResultStatus;
import lombok.Getter;

import java.util.Objects;

@Getter
public class ClusterOperationResult {

    private String jobId;

    private ResultStatus status;

    private String resource;

    private String resultContent;

    private boolean rolledBack;

    public ClusterOperationResult(String jobId, String resource) {
        this.jobId = jobId;
        this.resource = resource;
    }

    public boolean isOk() {
        return Objects.nonNull(this.status) && status.equals(ResultStatus.OK);
    }

    public void setResultContent(String resultContent) {
        this.resultContent = resultContent;
    }

    public void setStatus(ResultStatus status) {
        this.status = status;
    }

    public void setRolledBack(boolean rolledBack) {
        this.rolledBack = rolledBack;
    }
}
