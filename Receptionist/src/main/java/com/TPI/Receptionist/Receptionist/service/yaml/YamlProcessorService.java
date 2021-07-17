package com.TPI.Receptionist.Receptionist.service.yaml;

import com.TPI.Receptionist.Receptionist.core.CoreProcessor;
import com.TPI.Receptionist.Receptionist.core.ProcessRequestFactory;
import com.TPI.Receptionist.Receptionist.core.Script;
import com.TPI.Receptionist.Receptionist.core.dto.ProcessResultDto;
import com.TPI.Receptionist.Receptionist.service.ProcessorService;
import com.TPI.Receptionist.Receptionist.utils.YamlValidator;
import com.TPI.Receptionist.Receptionist.exceptions.InvalidYamlProcessRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class YamlProcessorService implements ProcessorService {

    @Autowired
    YamlValidator yamlValidator;

    @Autowired
    CoreProcessor coreProcessor;

    private Logger logger = LoggerFactory.getLogger(YamlProcessorService.class);

    public YamlProcessResult processRequest(List<MultipartFile> multipartFiles) {
        if (Objects.isNull(multipartFiles) || multipartFiles.isEmpty()) {
            throw new InvalidYamlProcessRequestException();
        }

        YamlProcessResult result = new YamlProcessResult();

        List<MultipartFile> invalidFiles = multipartFiles.parallelStream()
                .filter(yamlValidator::isInvalid).collect(Collectors.toList());

        if (invalidFiles.isEmpty()) {
            List<Script> scripts = new ArrayList<>();
            multipartFiles.forEach(file -> {
                try {
                    scripts.add(new Script(file.getOriginalFilename(), file.getBytes()));
                } catch (IOException e) {
                    logger.error("There's been an error reading file content - {}", e.getMessage());
                }
            });
            ProcessResultDto processResult = coreProcessor.process(ProcessRequestFactory.createProcessRequest(scripts));
            result.setProcessResult(processResult);
        }
        else {
            result.setInvalidFiles(invalidFiles);
        }

        return result;
    }
}
