package com.example.cucumber.bdd;

import com.example.cucumber.service.SystemStatusService;
import org.springframework.stereotype.Component;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

@Component
public class EndpointManagerServiceEmulator {

    private final SystemStatusService systemStatusService;

    public EndpointManagerServiceEmulator(SystemStatusService systemStatusService) {
        this.systemStatusService = systemStatusService;
    }

    public void resetMock() {
        reset(systemStatusService);
    }

    public void verifyOverrideCalledWith(String status) {
        verify(systemStatusService).overrideHealthStatus(status);
    }

    public void verifyReleaseOverrideCalled() {
        verify(systemStatusService).releaseOverride();
    }
}
