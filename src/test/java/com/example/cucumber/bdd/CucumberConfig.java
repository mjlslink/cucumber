package com.example.cucumber.bdd;

import com.example.cucumber.actuator.EpmHealthOverrideEndpoint;
import com.example.cucumber.service.EndpointManagerService;
import io.cucumber.spring.CucumberContextConfiguration;
import org.mockito.Mockito;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

@CucumberContextConfiguration
@SpringBootTest(classes = CucumberConfig.LibraryTestBootConfig.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(CucumberConfig.MockedServiceConfig.class)
public class CucumberConfig {

    @SpringBootConfiguration
    @EnableAutoConfiguration
    static class LibraryTestBootConfig {
        @Bean
        EpmHealthOverrideEndpoint epmHealthOverrideEndpoint(EndpointManagerService endpointManagerService) {
            return new EpmHealthOverrideEndpoint(endpointManagerService);
        }
    }

    @TestConfiguration
    static class MockedServiceConfig {
        @Bean
        @Primary
        EndpointManagerService endpointManagerService() {
            return Mockito.mock(EndpointManagerService.class);
        }
    }
}
