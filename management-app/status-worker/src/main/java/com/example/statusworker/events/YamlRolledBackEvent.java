package com.example.statusworker.events;

import com.example.commons.dto.YamlProcessResult;
import org.springframework.context.ApplicationEvent;

public class YamlRolledBackEvent extends ApplicationEvent {
    public YamlRolledBackEvent(YamlProcessResult request) {
        super(request);
    }

    public YamlProcessResult getSource() {
        return (YamlProcessResult) super.getSource();
    }
}
