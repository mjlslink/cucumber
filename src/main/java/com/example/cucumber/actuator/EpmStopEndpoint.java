package com.example.cucumber.actuator;

import com.example.cucumber.service.EndpointManagerService;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.boot.actuate.endpoint.web.WebEndpointResponse;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id = "epm-stop-endpont")
public class EpmStopEndpoint {

    private final EndpointManagerService endpointManagerService;

    public EpmStopEndpoint(EndpointManagerService endpointManagerService) {
        this.endpointManagerService = endpointManagerService;
    }

    @SuppressWarnings("unused")
    @WriteOperation
    public WebEndpointResponse<Object> stopEndpoint(String endpointName) {
        endpointManagerService.stopEndpoint(endpointName);
        return new WebEndpointResponse<>(null, WebEndpointResponse.STATUS_NO_CONTENT);
    }
}
