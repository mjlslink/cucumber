package com.example.cucumber.bdd;

import com.example.cucumber.service.EndpointManagerService;
import com.example.cucumber.service.SystemStatusService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
class EndpointManagerMockedServicesConfig {
    @Bean
    @Primary
    SystemStatusService systemStatusService() {
        return Mockito.mock(SystemStatusService.class);
    }

    @Bean
    @Primary
    EndpointManagerService endpointManagerService() {
        return Mockito.mock(EndpointManagerService.class);
    }
}
