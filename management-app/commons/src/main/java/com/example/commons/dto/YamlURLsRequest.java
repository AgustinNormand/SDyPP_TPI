package com.example.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class YamlURLsRequest implements Serializable {

    String jobId;
    String splitterUrl;
    String joinerUrl;
    String workerUrl;

}