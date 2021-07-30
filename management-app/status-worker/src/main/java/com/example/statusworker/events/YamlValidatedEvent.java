package com.example.statusworker.events;

import com.example.commons.dto.Task;
import org.springframework.context.ApplicationEvent;

public class YamlValidatedEvent extends ApplicationEvent {
    public YamlValidatedEvent(Task request) {
        super(request);
    }

    public Task getSource() {
        return (Task) super.getSource();
    }
}
