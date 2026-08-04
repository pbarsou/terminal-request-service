package com.desafio.terminalrequest.infrastructure.config;

import com.timgroup.statsd.NoOpStatsDClient;
import com.timgroup.statsd.StatsDClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration
public class TestContainersConfiguration {

  @Bean
  @ServiceConnection
  public PostgreSQLContainer<?> postgresSQLContainer() {
    return new PostgreSQLContainer<>("postgres:16-alpine")
        .withDatabaseName("terminal_request_service")
        .withUsername("postgres")
        .withPassword("postgres");
  }

  @Bean
  @Primary
  public StatsDClient mockStatsDClient() {
    return new NoOpStatsDClient();
  }
}
