Feature: EPM health override actuator

  Scenario: Override health status through the custom actuator endpoint
    Given the epm health override actuator is available
    When I override health status to "DOWN"
    Then the override request succeeds
    And EndpointManagerService receives overrideHealthStatus with "DOWN"
