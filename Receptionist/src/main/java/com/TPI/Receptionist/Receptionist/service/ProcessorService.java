package com.TPI.Receptionist.Receptionist.service;

import com.TPI.Receptionist.Receptionist.service.yaml.YamlProcessResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProcessorService {
    YamlProcessResult processRequest(List<MultipartFile> files);
}