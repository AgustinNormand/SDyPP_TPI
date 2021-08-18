package com.example.commons.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class YamlFilesRequest implements Serializable {

    byte[] splitter;
    byte[] joiner;
    byte[] worker;
    byte[] autoscaler;

    public YamlFilesRequest(byte[] splitter, byte[] joiner, byte[] worker) {
        this.splitter = splitter;
        this.joiner = joiner;
        this.worker = worker;
    }
}