package com.example.clusterApplier.events;

import com.example.commons.dto.YamlURLsRequest;
import org.springframework.context.ApplicationEvent;

public class TaskProcessedEvent extends ApplicationEvent {
    public TaskProcessedEvent(YamlURLsRequest source) {
        super(source);
    }

    public YamlURLsRequest getSource() {
        return (YamlURLsRequest) super.getSource();
    }
}
