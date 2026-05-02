package com.example.cucumber.bdd;

import com.example.cucumber.actuator.EpmLoadEndpoint;
import com.example.cucumber.actuator.EpmDeleteEndpoint;
import com.example.cucumber.actuator.EpmEndpoints;
import com.example.cucumber.actuator.EpmStartEndpoint;
import com.example.cucumber.actuator.health.EpmHealthReleaseOverrideEndpoint;
import com.example.cucumber.actuator.health.EpmHealthOverrideEndpoint;
import com.example.cucumber.service.EndpointManagerService;
import com.example.cucumber.service.SystemStatusService;
import io.cucumber.spring.CucumberContextConfiguration;
import org.mockito.Mockito;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

@CucumberContextConfiguration
@SpringBootTest(classes = CucumberSpringConfiguration.LibraryTestBootConfig.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(CucumberSpringConfiguration.MockedServiceConfig.class)
public class CucumberSpringConfiguration {

    @SpringBootConfiguration
    @EnableAutoConfiguration
    static class LibraryTestBootConfig {
        @Bean
        EpmHealthOverrideEndpoint epmHealthOverrideEndpoint(SystemStatusService systemStatusService) {
            return new EpmHealthOverrideEndpoint(systemStatusService);
        }

        @Bean
        EpmHealthReleaseOverrideEndpoint epmHealthReleaseOverrideEndpoint(SystemStatusService systemStatusService) {
            return new EpmHealthReleaseOverrideEndpoint(systemStatusService);
        }

        @Bean
        EpmLoadEndpoint epmLoadEndpoint(EndpointManagerService endpointManagerService) {
            return new EpmLoadEndpoint(endpointManagerService);
        }

        @Bean
        EpmDeleteEndpoint epmDeleteEndpoint(EndpointManagerService endpointManagerService) {
            return new EpmDeleteEndpoint(endpointManagerService);
        }

        @Bean
        EpmStartEndpoint epmStartEndpoint(EndpointManagerService endpointManagerService) {
            return new EpmStartEndpoint(endpointManagerService);
        }

        @Bean
        EpmEndpoints epmEndpoints(EndpointManagerService endpointManagerService) {
            return new EpmEndpoints(endpointManagerService);
        }

        @Bean
        EndpointManagerServiceEmulator endpointManagerServiceEmulator(
                SystemStatusService systemStatusService,
                EndpointManagerService endpointManagerService
        ) {
            return new EndpointManagerServiceEmulator(systemStatusService, endpointManagerService);
        }
    }

    @TestConfiguration
    static class MockedServiceConfig {
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
}

