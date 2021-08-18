package com.example.yamlManager.service.yaml.model;

import lombok.Getter;

@Getter
public class YamlValidated {

    private final byte[] serializedContent;

    private final String name;

    private YamlValidated(String name, byte[] serializedContent) {
        this.name = name;
        this.serializedContent = serializedContent;
    }
    public static YamlValidated of(String name, byte[] serializedContent) {
        return new YamlValidated(name, serializedContent);
    }
}
