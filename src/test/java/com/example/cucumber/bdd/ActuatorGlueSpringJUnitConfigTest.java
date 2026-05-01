package com.example.cucumber.bdd;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalManagementPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(classes = CucumberSpringConfiguration.LibraryTestBootConfig.class)
@SpringBootTest(classes = CucumberSpringConfiguration.LibraryTestBootConfig.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(CucumberSpringConfiguration.MockedServiceConfig.class)
@ActiveProfiles("test")
class ActuatorGlueSpringJUnitConfigTest {

    @LocalManagementPort
    private int managementPort;

    @Autowired
    private EndpointManagerServiceEmulator emulator;


    @Test
    void overrideFlowWorksThroughSpringJUnitConfig() {
        ActuatorGlue glue = new ActuatorGlue(managementPort, emulator);

        glue.beforeScenario();
        glue.actuatorAvailable();
        glue.overrideHealthStatus("DOWN");
        glue.overrideRequestSucceeds();
        glue.serviceReceivesOverride("DOWN");
    }

    @Test
    void releaseOverrideFlowWorksThroughSpringJUnitConfig() {
        ActuatorGlue glue = new ActuatorGlue(managementPort, emulator);

        glue.beforeScenario();
        glue.actuatorAvailable();
        glue.triggerHealthReleaseOverride();
        glue.overrideRequestSucceeds();
        glue.systemStatusServiceReceivesReleaseOverride();
    }
}

