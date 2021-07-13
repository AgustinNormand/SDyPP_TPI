package com.TPI.Receptionist.Receptionist.core;

import com.TPI.Receptionist.Receptionist.core.enums.ResultStatus;
import lombok.Getter;

public class ClusterOperationResult {

    @Getter
    private String jobId;

    private ResultStatus status;

    @Getter
    private String resource;

    @Getter
    private String resultContent;

    public ClusterOperationResult(String jobId, String resource) {
        this.jobId = jobId;
        this.resource = resource;
    }

    public boolean isOk() {
        return status.equals(ResultStatus.OK);
    }

    public void setOkStatus() {
        this.status = ResultStatus.OK;
    }

    public void setErrorStatus() {
        this.status = ResultStatus.ERROR;
    }

    public void setResultContent(String resultContent) {
        this.resultContent = resultContent;
    }
}
