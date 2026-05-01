package com.example.cucumber.bdd;

import io.cucumber.junit.CucumberOptions;

@CucumberOptions(
        features = "classpath:features",
        glue = "com.example.cucumber.bdd",
        plugin = {"pretty", "summary"}
)
public final class CucumberIdeConfiguration {

    private CucumberIdeConfiguration() {
        // Utility holder for IDE Cucumber step discovery metadata.
    }
}

