Feature: EPM health override actuator

  Scenario: Override health status through the custom actuator endpoint
    Given the actuator endpoint "/epm-health-endpont" is available
    When I call actuator endpoint "/epm-health-override" with JSON field "status" set to "DOWN"
    Then the actuator request returns status 204
    And SystemStatusService receives overrideHealthStatus with "DOWN"

  Scenario: Release health status override through the custom actuator endpoint
    Given the actuator endpoint "/epm-health-release-override" is available
    When I call actuator endpoint "/epm-health-release-override" without a body
    Then the actuator request returns status 204
    And SystemStatusService receives releaseOverride

  Scenario: Load endpoint settings through the custom actuator endpoint
    Given the actuator endpoint "/epm-load-endpoint" is available
    When I call actuator endpoint "/epm-load-endpoint" with JSON string field "json":
      """
      {"name":"payments","enabled":true}
      """
    Then the actuator request returns status 204
    And EndpointManagerService receives loadEndpoint with JSON
      """
      {"name":"payments","enabled":true}
      """

  Scenario: Start endpoint through the custom actuator endpoint
    Given the actuator endpoint "/epm-start-endpont" is available
    When I call actuator endpoint "/epm-start-endpont" with JSON field "endpointName" set to "payments"
    Then the actuator request returns status 204
    And EndpointManagerService receives startEndpoint with "payments"

  Scenario: Delete endpoint through the custom actuator endpoint
    Given the actuator endpoint "/epm-delete-endpont" is available
    When I call actuator endpoint "/epm-delete-endpont" with JSON field "endpointName" set to "payments"
    Then the actuator request returns status 204
    And EndpointManagerService receives deleteEndpoint with "payments"

  Scenario: List endpoints through the custom actuator endpoint
    Given the actuator endpoint "/epm-endpoints" is available
    When I call actuator endpoint "/epm-endpoints" with GET
    Then the actuator request returns status 200
    And EndpointManagerService receives getEndpoints

