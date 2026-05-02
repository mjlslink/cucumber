package com.example.cucumber.service;

import com.example.cucumber.endpoints.Endpoint;

import java.util.List;

public interface EndpointManagerService {

    void loadEndpoint(String json);
    void startEndpoint(String endpointName);
    void stopEndpoint(String endpointName);
    void deleteEndpoint(String endpointName);
    Endpoint getEndpoint(String endpointName);
    //void loadEndpoint(String json);
    List<Endpoint> getEndpoints();
}
