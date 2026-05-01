Feature: EPM health override actuator

  Scenario: Override health status through the custom actuator endpoint
    Given the epm health override actuator is available
    When I override health status to "DOWN"
    Then the override request succeeds
    And SystemStatusService receives overrideHealthStatus with "DOWN"

  Scenario: Release health status override through the custom actuator endpoint
    When I trigger epm health release override
    Then the override request succeeds
    And SystemStatusService receives releaseOverride
