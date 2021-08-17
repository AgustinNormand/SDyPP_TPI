package com.example.yamlManager.events;

import com.example.commons.dto.Task;
import org.springframework.context.ApplicationEvent;

public class TaskProcessedEvent extends ApplicationEvent {
    public TaskProcessedEvent(Task source) {
        super(source);
    }

    public Task getSource() {
        return (Task) super.getSource();
    }
}
