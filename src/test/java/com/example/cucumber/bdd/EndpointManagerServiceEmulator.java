package com.example.cucumber.bdd;

import com.example.cucumber.service.EndpointManagerService;
import org.springframework.stereotype.Component;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

@Component
public class EndpointManagerServiceEmulator {

    private final EndpointManagerService endpointManagerService;

    public EndpointManagerServiceEmulator(EndpointManagerService endpointManagerService) {
        this.endpointManagerService = endpointManagerService;
    }

    public void resetMock() {
        reset(endpointManagerService);
    }

    public void verifyOverrideCalledWith(String status) {
        verify(endpointManagerService).overrideHealthStatus(status);
    }
}
