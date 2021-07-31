package com.example.clusterApplier.events;

import com.example.commons.dto.Task;
import org.springframework.context.ApplicationEvent;

public class RollbackTaskReceivedEvent extends ApplicationEvent {
    public RollbackTaskReceivedEvent(Task request) {
        super(request);
    }

    public Task getSource() {
        return (Task) super.getSource();
    }
}
