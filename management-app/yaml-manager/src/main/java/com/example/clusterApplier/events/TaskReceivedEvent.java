package com.example.clusterApplier.events;

import com.example.commons.dto.Task;
import org.springframework.context.ApplicationEvent;

public class TaskReceivedEvent extends ApplicationEvent {
    public TaskReceivedEvent(Task source) {
        super(source);
    }

    public Task getSource() {
        return (Task) super.getSource();
    }
}
