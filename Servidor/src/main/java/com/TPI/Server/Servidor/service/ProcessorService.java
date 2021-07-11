package com.TPI.Server.Servidor.service;

import com.TPI.Server.Servidor.service.yaml.YamlProcessResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProcessorService {
    YamlProcessResult processRequest(List<MultipartFile> files);
}