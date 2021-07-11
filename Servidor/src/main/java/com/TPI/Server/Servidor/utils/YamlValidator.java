package com.TPI.Server.Servidor.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class YamlValidator {

    public boolean isValid(MultipartFile multipartFile) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            mapper.readValue(multipartFile.getBytes(), Object.class);
            return true;
        }
        catch (IOException e){
            return false;
        }
    }
}
