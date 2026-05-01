package com.example.cucumber.actuator;

import com.example.cucumber.service.EndpointManagerService;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;

import java.util.Map;

@Component
@Endpoint(id = "epm-health-override")
public class EpmHealthOverrideEndpoint {

    private final EndpointManagerService endpointManagerService;

    public EpmHealthOverrideEndpoint(EndpointManagerService endpointManagerService) {
        this.endpointManagerService = endpointManagerService;
    }

    @WriteOperation
    public Web<String, String> override(String status) {
        endpointManagerService.overrideHealthStatus(status);
        return Map.of("acceptedStatus", status);
    }
}
