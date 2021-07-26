package com.example.clusterApplier.service.yaml;

import com.example.clusterApplier.core.CoreProcessor;
import com.example.clusterApplier.core.ProcessRequestFactory;
import com.example.clusterApplier.core.dto.ProcessResultDto;
import com.example.clusterApplier.exceptions.InvalidYamlProcessRequestException;
import com.example.commons.dto.YamlURLsRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class YamlProcessorService {

    @Autowired
    CoreProcessor coreProcessor;

    private Logger logger = LoggerFactory.getLogger(YamlProcessorService.class);

    public YamlProcessResult processRequest(YamlURLsRequest request) {
        if (Objects.isNull(request)) {
            throw new InvalidYamlProcessRequestException();
        }

        YamlProcessResult result = new YamlProcessResult();

        String splitter = request.getSplitterUrl();
        String worker = request.getWorkerUrl();
        String joiner = request.getJoinerUrl();

        ProcessResultDto processResult = coreProcessor
                .process(ProcessRequestFactory.createProcessRequest(splitter, worker, joiner));
        result.setProcessResult(processResult);

        return result;
    }
}
