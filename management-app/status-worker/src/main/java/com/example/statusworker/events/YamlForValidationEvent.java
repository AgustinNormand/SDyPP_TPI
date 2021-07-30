package com.example.statusworker.events;

import com.example.commons.dto.Task;
import org.springframework.context.ApplicationEvent;

public class YamlForValidationEvent extends ApplicationEvent {
    public YamlForValidationEvent(Task request) {
        super(request);
    }

    public Task getSource() {
        return (Task) super.getSource();
    }
}
