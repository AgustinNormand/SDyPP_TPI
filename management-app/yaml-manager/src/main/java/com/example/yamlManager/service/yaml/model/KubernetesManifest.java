package com.example.yamlManager.service.yaml.model;

public class KubernetesManifest {

    private String kind;

    private Spec spec;

    private class Spec {
        private String replicas;
    }
}
