package com.example.cucumber.bdd;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ActuatorGlue {

    private final MockMvc mockMvc;
    private final EndpointManagerServiceEmulator emulator;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private ResultActions response;

    public ActuatorGlue(MockMvc mockMvc, EndpointManagerServiceEmulator emulator) {
        this.mockMvc = mockMvc;
        this.emulator = emulator;
    }

    @Before
    public void beforeScenario() {
        emulator.resetMock();
    }

    @Given("the epm health override actuator is available")
    public void actuatorAvailable() {
        // Context bootstrapping is handled by CucumberSpringConfiguration.
    }

    @When("I override health status to {string}")
    public void overrideHealthStatus(String status) throws Exception {
        response = mockMvc.perform(post("/epp/netauths/h2/api/actuator/epm-health-override")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("status", status))));
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

