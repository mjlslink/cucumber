# cucumber

Gradle-based library-style Spring Boot + Cucumber example that tests a custom actuator endpoint named `epm-health-override`.

## What is included

- Custom actuator endpoint that delegates to EndpointManagerService.overrideHealthStatus(String status)
- Cucumber test flow with:
	- CucumberSpringConfiguration (the only place using SpringBootTest)
	- CucumberRunner (JUnit Platform suite)
	- EndpointManagerServiceEmulator (mock helper)
	- Step definitions and feature file

## Run tests

```bash
./gradlew test
```