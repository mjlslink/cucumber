package com.example.cucumber.bdd;

import com.example.cucumber.actuator.health.EpmHealthReleaseOverrideEndpoint;
import com.example.cucumber.actuator.health.EpmHealthOverrideEndpoint;
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
        EndpointManagerServiceEmulator endpointManagerServiceEmulator(SystemStatusService systemStatusService) {
            return new EndpointManagerServiceEmulator(systemStatusService);
        }
    }

    @TestConfiguration
    static class MockedServiceConfig {
        @Bean
        @Primary
        SystemStatusService systemStatusService() {
            return Mockito.mock(SystemStatusService.class);
        }
    }
}

