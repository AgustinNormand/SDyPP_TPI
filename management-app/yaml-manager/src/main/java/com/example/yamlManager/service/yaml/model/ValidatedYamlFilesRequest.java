package com.example.yamlManager.service.yaml.model;

import com.example.commons.dto.YamlFilesRequest;
import lombok.Getter;

@Getter
public class ValidatedYamlFilesRequest extends YamlFilesRequest {

    private String splitterName;
    private String joinerName;
    private String workerName;

    public ValidatedYamlFilesRequest(YamlValidated splitter, YamlValidated joiner, YamlValidated worker) {
        super(splitter.getSerializedContent(), joiner.getSerializedContent(), worker.getSerializedContent());
        this.splitterName = splitter.getName();
        this.joinerName = joiner.getName();
        this.workerName = worker.getName();
    }

    public ValidatedYamlFilesRequest() {

    }

    public static ValidatedYamlFilesRequest empty() {
        return new ValidatedYamlFilesRequest();
    }

    public YamlFilesRequest yamlFilesRequest() {
        return new YamlFilesRequest(super.getSplitter(), super.getJoiner(), super.getWorker(), super.getAutoscaler());
    }
}
