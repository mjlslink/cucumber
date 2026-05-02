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

    private static final String LOAD_ENDPOINT_JSON = "{\"name\":\"payments\",\"enabled\":true}";
    private static final String LOAD_ENDPOINT_PRETTY_JSON = "{\n"
            + "  \"name\": \"payments\",\n"
            + "  \"enabled\": true\n"
            + "}";
    private static final String START_ENDPOINT_NAME = "payments";
    private static final String DELETE_ENDPOINT_NAME = "payments";

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

    @Test
    void loadFlowWorksThroughSpringJUnitConfig() {
        ActuatorGlue glue = new ActuatorGlue(managementPort, emulator);

        glue.beforeScenario();
        glue.loadActuatorAvailable();
        glue.loadEndpointSettings(LOAD_ENDPOINT_JSON);
        glue.overrideRequestSucceeds();
        glue.endpointManagerServiceReceivesLoadEndpointWithJson(LOAD_ENDPOINT_JSON);
    }

    @Test
    void loadFlowSerializesAndDeserializesJsonThroughWebClient() {
        ActuatorGlue glue = new ActuatorGlue(managementPort, emulator);

        glue.beforeScenario();
        glue.loadActuatorAvailable();
        glue.loadEndpointSettings(LOAD_ENDPOINT_PRETTY_JSON);
        glue.overrideRequestSucceeds();
        glue.endpointManagerServiceReceivesLoadEndpointWithJson(LOAD_ENDPOINT_PRETTY_JSON);
    }

    @Test
    void startFlowWorksThroughSpringJUnitConfig() {
        ActuatorGlue glue = new ActuatorGlue(managementPort, emulator);

        glue.beforeScenario();
        glue.startActuatorAvailable();
        glue.callActuatorEndpointWithSingleJsonField("/epm-start-override", "endpointName", START_ENDPOINT_NAME);
        glue.overrideRequestSucceeds();
        glue.endpointManagerServiceReceivesStartEndpointWith(START_ENDPOINT_NAME);
    }

    @Test
    void endpointsFlowWorksThroughSpringJUnitConfig() {
        ActuatorGlue glue = new ActuatorGlue(managementPort, emulator);

        glue.beforeScenario();
        glue.callActuatorEndpointWithGet("/epm-endpoints");
        glue.actuatorRequestReturnsStatus(200);
        glue.endpointManagerServiceReceivesGetEndpoints();
    }

    @Test
    void deleteFlowWorksThroughSpringJUnitConfig() {
        ActuatorGlue glue = new ActuatorGlue(managementPort, emulator);

        glue.beforeScenario();
        glue.callActuatorEndpointWithSingleJsonField("/epm-delete-endpont", "endpointName", DELETE_ENDPOINT_NAME);
        glue.overrideRequestSucceeds();
        glue.endpointManagerServiceReceivesDeleteEndpointWith(DELETE_ENDPOINT_NAME);
    }
}

