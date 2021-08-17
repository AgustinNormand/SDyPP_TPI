package com.example.yamlManager;

import com.example.yamlManager.utils.YamlAutoScalerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class YamlAutoScalerFactoryTests {

    private YamlAutoScalerFactory factory;

    @BeforeEach
    private void setup() {
        this.factory = new YamlAutoScalerFactory();
    }

    @Test
    public void yamlAutoScalerFactoryTest() {
        factory.createAutoScaler("worker-example");
    }

}
