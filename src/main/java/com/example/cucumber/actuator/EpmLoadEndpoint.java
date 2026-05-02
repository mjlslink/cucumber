package com.example.cucumber.actuator;

import com.example.cucumber.endpoints.EndpointSettings;
import com.example.cucumber.service.EndpointManagerService;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.boot.actuate.endpoint.web.WebEndpointResponse;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id = "epm-load-endpoint")
public class EpmLoadEndpoint {

    private final EndpointManagerService endpointManagerService;

    public EpmLoadEndpoint(EndpointManagerService endpointManagerService) {
        this.endpointManagerService = endpointManagerService;
    }

    @SuppressWarnings("unused")
    @WriteOperation
    public WebEndpointResponse<Object> loadEndpoint(String json) {
        EndpointSettings.fromJson(json);
        endpointManagerService.loadEndpoint(json);
        return new WebEndpointResponse<>(null, WebEndpointResponse.STATUS_NO_CONTENT);
    }
}
