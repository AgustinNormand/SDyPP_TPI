package com.example.clusterApplier.events;

import com.example.commons.dto.YamlURLsRequest;
import org.springframework.context.ApplicationEvent;

public class RequestReceivedEvent extends ApplicationEvent {
    public RequestReceivedEvent(YamlURLsRequest source) {
        super(source);
    }

    public YamlURLsRequest getSource() {
        return (YamlURLsRequest) super.getSource();
    }
}
