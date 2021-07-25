package com.example.clusterApplier.core;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a k8s script that can be applied in the cluster
 */
@Data
@AllArgsConstructor
public class Script {

    private String filename;

    private String url;

}