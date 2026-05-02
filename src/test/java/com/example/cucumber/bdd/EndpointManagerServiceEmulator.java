package com.example.cucumber.bdd;

import com.example.cucumber.service.EndpointManagerService;
import com.example.cucumber.service.SystemStatusService;
import org.springframework.stereotype.Component;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

@Component
public class EndpointManagerServiceEmulator {

    private final SystemStatusService systemStatusService;
    private final EndpointManagerService endpointManagerService;

    public EndpointManagerServiceEmulator(
            SystemStatusService systemStatusService,
            EndpointManagerService endpointManagerService
    ) {
        this.systemStatusService = systemStatusService;
        this.endpointManagerService = endpointManagerService;
    }

    public void resetMock() {
        reset(systemStatusService);
        reset(endpointManagerService);
    }

    public void verifyOverrideCalledWith(String status) {
        verify(systemStatusService).overrideHealthStatus(status);
    }

    public void verifyReleaseOverrideCalled() {
        verify(systemStatusService).releaseOverride();
    }

    public void verifyLoadEndpointCalledWith(String json) {
        verify(endpointManagerService).loadEndpoint(json);
    }

    public void verifyStartEndpointCalledWith(String endpointName) {
        verify(endpointManagerService).startEndpoint(endpointName);
    }

    public void verifyGetEndpointsCalled() {
        verify(endpointManagerService).getEndpoints();
    }

    public void verifyDeleteEndpointCalledWith(String endpointName) {
        verify(endpointManagerService).deleteEndpoint(endpointName);
    }
}
