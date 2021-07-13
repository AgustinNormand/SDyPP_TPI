package com.TPI.Receptionist.Receptionist.service.yaml;

import com.TPI.Receptionist.Receptionist.core.CoreProcessor;
import com.TPI.Receptionist.Receptionist.core.ProcessRequestFactory;
import com.TPI.Receptionist.Receptionist.core.ProcessResult;
import com.TPI.Receptionist.Receptionist.core.Script;
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
                .filter(yamlValidator::isValid).collect(Collectors.toList());

        if (invalidFiles.isEmpty()) {
            List<Script> files = new ArrayList<>();
            for (MultipartFile file : multipartFiles) {
                try {
                    files.add(new Script(file.getOriginalFilename(), file.getInputStream()));
                }
                catch (IOException e){
                    logger.error("Error reading from file: {}", e.getMessage());
                    throw new InvalidYamlProcessRequestException(String.format("Error reading from file: %s", file.getOriginalFilename()));
                }
            }
            ProcessResult processResult = coreProcessor.process(ProcessRequestFactory.createProcessRequest(files));
            result.setProcessResult(processResult);
        }
        else {
            result.setInvalidFiles(invalidFiles);
        }

        return result;
    }
}
