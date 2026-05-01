package com.example.cucumber.actuator.health;

import com.example.cucumber.service.SystemStatusService;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.boot.actuate.endpoint.web.WebEndpointResponse;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id = "epm-health-override")
public class EpmHealthOverrideEndpoint {

    private final SystemStatusService systemStatusService;

    public EpmHealthOverrideEndpoint(SystemStatusService systemStatusService) {
        this.systemStatusService = systemStatusService;
    }

    @SuppressWarnings("unused")
    @WriteOperation
    public WebEndpointResponse<Object> override(String status) {
        systemStatusService.overrideHealthStatus(status);
        return new WebEndpointResponse<>(null, WebEndpointResponse.STATUS_NO_CONTENT);
    }
}
