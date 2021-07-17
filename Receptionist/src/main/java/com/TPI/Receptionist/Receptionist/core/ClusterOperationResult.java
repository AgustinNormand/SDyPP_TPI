package com.TPI.Receptionist.Receptionist.core;

import com.TPI.Receptionist.Receptionist.core.enums.ResultStatus;
import lombok.Getter;

import java.util.Objects;

@Getter
public class ClusterOperationResult {

    private String jobId;

    private ResultStatus status;

    private String resource;

    private String resultContent;

    public ClusterOperationResult(String jobId, String resource) {
        this.jobId = jobId;
        this.resource = resource;
    }

    public boolean isOk() {
        return Objects.nonNull(this.status) && status.equals(ResultStatus.OK);
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
