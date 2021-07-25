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

}