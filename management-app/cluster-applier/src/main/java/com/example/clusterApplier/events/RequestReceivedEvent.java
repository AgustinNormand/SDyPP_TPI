package com.example.clusterApplier.events;

import com.example.commons.dto.Task;
import com.example.commons.dto.YamlURLsRequest;
import org.springframework.context.ApplicationEvent;

public class RequestReceivedEvent extends ApplicationEvent {
    public RequestReceivedEvent(Task source) {
        super(source);
    }

    public Task getSource() {
        return (Task) super.getSource();
    }
}
