package com.example.statusworker.handlers;

import com.example.commons.dto.JobStatus;
import com.example.commons.dto.YamlProcessResult;
import com.example.statusworker.events.YamlRolledBackEvent;
import com.example.statusworker.service.RedisService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class YamlRolledBackHandler {

    @Autowired
    RedisService redis;

    private Logger logger = LoggerFactory.getLogger(YamlRolledBackHandler.class);

    @EventListener
    public void onYamlRolledBackHandler(YamlRolledBackEvent event) {
        try {
            YamlProcessResult yamlProcessResult = event.getSource();

            String body = new ObjectMapper().writeValueAsString(yamlProcessResult.getProcessResult());
            redis.save(yamlProcessResult.getJobId(), JobStatus.State.ROLLED_BACK, body);
        }
        catch (JsonProcessingException e) {
            logger.error("There's been an error trying to convert process result to string - {}", e.getMessage());
        }
    }

}
