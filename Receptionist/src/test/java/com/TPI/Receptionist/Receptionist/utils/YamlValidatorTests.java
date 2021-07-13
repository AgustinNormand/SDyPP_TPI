package com.TPI.Receptionist.Receptionist.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class YamlValidatorTests {

    private YamlValidator yamlValidator;

    @BeforeEach
    public void setup() {
        this.yamlValidator = new YamlValidator();
    }

    @Test
    public void whenValidateYaml_givenCorrectYaml_shouldReturnTrue() throws IOException {

        File file = new File("src/test/resources/valid.yaml");

        MultipartFile multipartFile = new MockMultipartFile("valid", new FileInputStream(file));

        Assertions.assertTrue(this.yamlValidator.isValid(multipartFile));
    }

    @Test
    public void whenValidateYaml_givenWrongYaml_shouldReturnFalse() throws IOException {
        File file = new File("src/test/resources/invalid.yaml");

        MultipartFile multipartFile = new MockMultipartFile("valid", new FileInputStream(file));

        Assertions.assertFalse(this.yamlValidator.isValid(multipartFile));
    }

}
