package com.example.cucumber.bdd;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.boot.test.web.server.LocalManagementPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ActuatorGlue {

    private static final String MANAGEMENT_BASE_PATH = "/epp/netauths/h2/api/actuator";
    private static final String ACTUATOR_OVERRIDE_PATH = "/epm-health-override";
    private static final String ACTUATOR_RELEASE_OVERRIDE_PATH = "/epm-health-release-override";

    private final int managementPort;
    private final EndpointManagerServiceEmulator emulator;
    private WebClient webClient;
    private ResponseEntity<Void> response;

    public ActuatorGlue(@LocalManagementPort int managementPort, EndpointManagerServiceEmulator emulator) {
        this.managementPort = managementPort;
        this.emulator = emulator;
    }

    @Before
    public void beforeScenario() {
        emulator.resetMock();
        if (managementPort <= 0) {
            throw new IllegalStateException("No local management port is available in the test context");
        }
        webClient = WebClient.builder().build();
    }

    @Given("the epm health override actuator is available")
    public void actuatorAvailable() {
        // Context bootstrapping is handled by CucumberSpringConfiguration.
    }

    @When("I override health status to {string}")
    public void overrideHealthStatus(String status) {
        callActuatorWithBody(ACTUATOR_OVERRIDE_PATH, Map.of("status", status));
    }

    @When("I call actuator endpoint {string} with JSON field {string} set to {string}")
    public void callActuatorEndpointWithSingleJsonField(String endpointPath, String field, String value) {
        callActuatorWithBody(endpointPath, Map.of(field, value));
    }

    @When("I trigger epm health release override")
    public void triggerHealthReleaseOverride() {
        callActuatorWithoutBody(ACTUATOR_RELEASE_OVERRIDE_PATH);
    }

    @When("I call actuator endpoint {string} without a body")
    public void callActuatorEndpointWithoutBody(String endpointPath) {
        callActuatorWithoutBody(endpointPath);
    }

    @Then("the override request succeeds")
    public void overrideRequestSucceeds() {
        actuatorRequestReturnsStatus(HttpStatus.NO_CONTENT.value());
    }

    @Then("the actuator request returns status {int}")
    public void actuatorRequestReturnsStatus(int expectedStatusCode) {
        assertEquals(expectedStatusCode, response.getStatusCode().value());
    }

    @Then("EndpointManagerService receives overrideHealthStatus with {string}")
    public void serviceReceivesOverride(String status) {
        emulator.verifyOverrideCalledWith(status);
    }

    @Then("SystemStatusService receives overrideHealthStatus with {string}")
    public void systemStatusServiceReceivesOverride(String status) {
        emulator.verifyOverrideCalledWith(status);
    }

    @Then("SystemStatusService receives releaseOverride")
    public void systemStatusServiceReceivesReleaseOverride() {
        emulator.verifyReleaseOverrideCalled();
    }

    private void callActuatorWithBody(String endpointPath, Object body) {
        String normalizedEndpointPath = endpointPath.startsWith("/") ? endpointPath : "/" + endpointPath;
        String endpointUrl = "http://localhost:" + managementPort + MANAGEMENT_BASE_PATH + normalizedEndpointPath;
        response = webClient.post()
                .uri(endpointUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    private void callActuatorWithoutBody(String endpointPath) {
        String normalizedEndpointPath = endpointPath.startsWith("/") ? endpointPath : "/" + endpointPath;
        String endpointUrl = "http://localhost:" + managementPort + MANAGEMENT_BASE_PATH + normalizedEndpointPath;
        response = webClient.post()
                .uri(endpointUrl)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}

