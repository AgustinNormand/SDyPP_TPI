package com.example.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StepStatus {

    private JobStatus.State state;

    private String content;

}
