package com.example.clusterApplier.events;

import com.example.commons.dto.ProcessResultDto;
import com.example.commons.dto.Task;
import org.springframework.context.ApplicationEvent;

public class YamlRolledBackEvent extends ApplicationEvent {
    public YamlRolledBackEvent(ProcessResultDto result) {
        super(result);
    }

    public ProcessResultDto getSource() {
        return (ProcessResultDto) super.getSource();
    }

}
