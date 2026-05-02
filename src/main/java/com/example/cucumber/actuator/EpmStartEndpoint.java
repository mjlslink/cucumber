package com.example.cucumber.actuator;

import com.example.cucumber.service.EndpointManagerService;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.boot.actuate.endpoint.web.WebEndpointResponse;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id = "epm-start-endpont")
public class EpmStartEndpoint {

    private final EndpointManagerService endpointManagerService;

    public EpmStartEndpoint(EndpointManagerService endpointManagerService) {
        this.endpointManagerService = endpointManagerService;
    }

    @SuppressWarnings("unused")
    @WriteOperation
    public WebEndpointResponse<Object> startEndpoint(String endpointName) {
        endpointManagerService.startEndpoint(endpointName);
        return new WebEndpointResponse<>(null, WebEndpointResponse.STATUS_NO_CONTENT);
    }
}
