package com.example.clusterApplier.events;

import com.example.clusterApplier.service.yaml.YamlProcessResult;
import org.springframework.context.ApplicationEvent;

public class YamlAppliedEvent extends ApplicationEvent {
    public YamlAppliedEvent(YamlProcessResult source) {
        super(source);
    }

    public YamlProcessResult getSource() {
        return (YamlProcessResult) super.getSource();
    }

}
