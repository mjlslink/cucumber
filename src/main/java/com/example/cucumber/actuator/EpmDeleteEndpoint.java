package com.example.cucumber.actuator;

import com.example.cucumber.service.EndpointManagerService;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.boot.actuate.endpoint.web.WebEndpointResponse;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id = "epm-delete-endpont")
public class EpmDeleteEndpoint {

    private final EndpointManagerService endpointManagerService;

    public EpmDeleteEndpoint(EndpointManagerService endpointManagerService) {
        this.endpointManagerService = endpointManagerService;
    }

    @SuppressWarnings("unused")
    @WriteOperation
    public WebEndpointResponse<Object> deleteEndpoint(String endpointName) {
        endpointManagerService.deleteEndpoint(endpointName);
        return new WebEndpointResponse<>(null, WebEndpointResponse.STATUS_NO_CONTENT);
    }
}
