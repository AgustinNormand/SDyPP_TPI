package com.example.splitter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    private String uuid;

    private Integer taskNumber;

    private Integer number;

}