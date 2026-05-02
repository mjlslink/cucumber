package com.example.cucumber.actuator;

import com.example.cucumber.service.EndpointManagerService;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.web.WebEndpointResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Endpoint(id = "epm-endpoints")
public class EpmEndpoints {

    private final EndpointManagerService endpointManagerService;

    public EpmEndpoints(EndpointManagerService endpointManagerService) {
        this.endpointManagerService = endpointManagerService;
    }

    @ReadOperation
    public WebEndpointResponse<Object> getEndpoints() {
        List<com.example.cucumber.endpoints.Endpoint> endpoints = endpointManagerService.getEndpoints();
        return new WebEndpointResponse<>(endpoints, WebEndpointResponse.STATUS_OK);
    }

}
