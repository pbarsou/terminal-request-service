package com.desafio.terminalrequest.infrastructure.config.datadog;

import com.timgroup.statsd.NonBlockingStatsDClientBuilder;
import com.timgroup.statsd.StatsDClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatadogConfig {

  @Bean(destroyMethod = "stop")
  public StatsDClient statsDClient(
      @Value("${datadog.statsd.host:datadog-agent}") String host,
      @Value("${datadog.statsd.port:8125}") int port,
      @Value("${spring.application.name}") String serviceName,
      @Value("${DD_ENV:local}") String env) {
    return new NonBlockingStatsDClientBuilder()
        .hostname(host)
        .port(port)
        .constantTags("service:" + serviceName, "env:" + env)
        .build();
  }
}
