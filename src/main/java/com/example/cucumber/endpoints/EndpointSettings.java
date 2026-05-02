package com.example.cucumber.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EndpointSettings {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static EndpointSettings fromJdon(String json) {
        return fromJson(json);
    }

    public static EndpointSettings fromJson(String json) {
        try {
            return OBJECT_MAPPER.readValue(json, EndpointSettings.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid endpoint settings JSON", e);
        }
    }
}
