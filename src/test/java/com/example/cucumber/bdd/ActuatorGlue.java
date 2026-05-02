package com.example.cucumber.bdd;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private static final String ACTUATOR_LOAD_ENDPOINT_PATH = "/epm-load-endpoint";
    private static final String ACTUATOR_START_ENDPOINT_PATH = "/epm-start-endpoint";
    private static final String ACTUATOR_DELETE_ENDPOINT_PATH = "/epm-delete-endpont";
    private static final String ACTUATOR_ENDPOINTS_PATH = "/epm-endpoints";

    private final int managementPort;
    private final EndpointManagerServiceEmulator emulator;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private WebClient webClient;
    private ResponseEntity<String> response;

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
        actuatorEndpointIsAvailable(ACTUATOR_OVERRIDE_PATH);
    }

    @Given("the epm load override actuator is available")
    public void loadActuatorAvailable() {
        actuatorEndpointIsAvailable(ACTUATOR_LOAD_ENDPOINT_PATH);
    }

    @Given("the epm start override actuator is available")
    public void startActuatorAvailable() {
        actuatorEndpointIsAvailable(ACTUATOR_START_ENDPOINT_PATH);
    }

    @Given("the epm delete actuator is available")
    public void deleteActuatorAvailable() {
        actuatorEndpointIsAvailable(ACTUATOR_DELETE_ENDPOINT_PATH);
    }

    @Given("the epm endpoints actuator is available")
    public void endpointsActuatorAvailable() {
        actuatorEndpointIsAvailable(ACTUATOR_ENDPOINTS_PATH);
    }

    @Given("the actuator endpoint {string} is available")
    public void actuatorEndpointIsAvailable(String endpointPath) {
        if (endpointPath == null || endpointPath.isBlank()) {
            throw new IllegalArgumentException("Actuator endpoint path must not be blank");
        }
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

    @When("I call actuator endpoint {string} with GET")
    public void callActuatorEndpointWithGet(String endpointPath) {
        callActuatorWithGet(endpointPath);
    }

    @When("I load endpoint settings")
    public void loadEndpointSettings(String json) {
        callActuatorEndpointWithJsonStringField(ACTUATOR_LOAD_ENDPOINT_PATH, "json", normalizeJson(json));
    }

    @When("I call actuator endpoint {string} with JSON string field {string}:")
    public void callActuatorEndpointWithJsonStringField(String endpointPath, String field, String json) {
        callActuatorWithBody(endpointPath, Map.of(field, json));
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

    @Then("EndpointManagerService receives loadEndpoint with JSON")
    public void endpointManagerServiceReceivesLoadEndpointWithJson(String json) {
        emulator.verifyLoadEndpointCalledWith(normalizeJson(json));
    }

    @Then("EndpointManagerService receives startEndpoint with {string}")
    public void endpointManagerServiceReceivesStartEndpointWith(String endpointName) {
        emulator.verifyStartEndpointCalledWith(endpointName);
    }

    @Then("EndpointManagerService receives getEndpoints")
    public void endpointManagerServiceReceivesGetEndpoints() {
        emulator.verifyGetEndpointsCalled();
    }

    @Then("EndpointManagerService receives deleteEndpoint with {string}")
    public void endpointManagerServiceReceivesDeleteEndpointWith(String endpointName) {
        emulator.verifyDeleteEndpointCalledWith(endpointName);
    }

    private void callActuatorWithBody(String endpointPath, Object body) {
        String normalizedEndpointPath = endpointPath.startsWith("/") ? endpointPath : "/" + endpointPath;
        String endpointUrl = "http://localhost:" + managementPort + MANAGEMENT_BASE_PATH + normalizedEndpointPath;
        String jsonBody = toJson(body);
        response = webClient.post()
                .uri(endpointUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonBody)
                .retrieve()
                .toEntity(String.class)
                .block();
    }

    private void callActuatorWithoutBody(String endpointPath) {
        String normalizedEndpointPath = endpointPath.startsWith("/") ? endpointPath : "/" + endpointPath;
        String endpointUrl = "http://localhost:" + managementPort + MANAGEMENT_BASE_PATH + normalizedEndpointPath;
        response = webClient.post()
                .uri(endpointUrl)
                .retrieve()
                .toEntity(String.class)
                .block();
    }

    private void callActuatorWithGet(String endpointPath) {
        String normalizedEndpointPath = endpointPath.startsWith("/") ? endpointPath : "/" + endpointPath;
        String endpointUrl = "http://localhost:" + managementPort + MANAGEMENT_BASE_PATH + normalizedEndpointPath;
        response = webClient.get()
                .uri(endpointUrl)
                .retrieve()
                .toEntity(String.class)
                .block();
    }

    private String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Unable to serialize request payload", e);
        }
    }

    private String normalizeJson(String json) {
        try {
            return objectMapper.readTree(json).toString();
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid JSON payload provided", e);
        }
    }
}

