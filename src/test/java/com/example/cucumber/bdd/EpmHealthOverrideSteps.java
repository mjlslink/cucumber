package com.example.cucumber.bdd;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EpmHealthOverrideSteps {

    private final MockMvc mockMvc;
    private final EndpointManagerServiceEmulator emulator;
    private final ObjectMapper yamlObjectMapper = new ObjectMapper(new YAMLFactory());
    private ResultActions response;

    public EpmHealthOverrideSteps(MockMvc mockMvc, EndpointManagerServiceEmulator emulator) {
        this.mockMvc = mockMvc;
        this.emulator = emulator;
    }

    @Before
    public void beforeScenario() {
        emulator.resetMock();
    }

    @Given("the epm health override actuator is available")
    public void actuatorAvailable() {
        // Context bootstrapping is handled by CucumberConfig.
    }

    @When("I override health status to {string}")
    public void overrideHealthStatus(String status) throws Exception {
        String yamlPayload = yamlObjectMapper.writeValueAsString(Map.of("status", status));
        Map<String, String> parsedPayload = yamlObjectMapper.readValue(yamlPayload, new TypeReference<>() {
        });

        response = mockMvc.perform(post("/actuator/epm-health-override")
                .param("status", parsedPayload.get("status")));
    }

    @Then("the override request succeeds")
    public void overrideRequestSucceeds() throws Exception {
        response.andExpect(status().isNoContent());
    }

    @Then("EndpointManagerService receives overrideHealthStatus with {string}")
    public void serviceReceivesOverride(String status) {
        emulator.verifyOverrideCalledWith(status);
    }
}
