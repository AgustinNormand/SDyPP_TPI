package com.example.clusterApplier.events;

import com.example.commons.dto.Task;
import com.example.commons.dto.YamlURLsRequest;
import org.springframework.context.ApplicationEvent;

public class ApplyTaskReceivedEvent extends ApplicationEvent {
    public ApplyTaskReceivedEvent(Task source) {
        super(source);
    }

    public Task getSource() {
        return (Task) super.getSource();
    }
}
