package com.example.cucumber.service;

public interface SystemStatusService {
    void overrideHealthStatus(String status);

    void releaseOverride();
}
