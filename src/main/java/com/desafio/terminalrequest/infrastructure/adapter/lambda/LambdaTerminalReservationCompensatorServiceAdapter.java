package com.desafio.terminalrequest.infrastructure.adapter.lambda;

import com.desafio.terminalrequest.domain.exceptions.TerminalReservationCompensationFailureException;
import com.desafio.terminalrequest.domain.service.TerminalReservationCompensatorServicePort;
import com.desafio.terminalrequest.infrastructure.config.datadog.event.StatsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LambdaTerminalReservationCompensatorServiceAdapter
    implements TerminalReservationCompensatorServicePort {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final HttpClient httpClient;
  private final ObjectMapper objectMapper;
  private final String functionUrl;
  private final StatsService statsService;

  public LambdaTerminalReservationCompensatorServiceAdapter(
      HttpClient httpClient,
      ObjectMapper objectMapper,
      @Value("${aws.lambda.terminal-reservation-compensator-function}") String functionUrl,
      StatsService statsService) {
    this.httpClient = httpClient;
    this.objectMapper = objectMapper;
    this.functionUrl = functionUrl.trim();
    this.statsService = statsService;
  }

  @Override
  public void release(UUID terminalId, UUID terminalRequestId) {
    try {
      var payload =
          objectMapper.writeValueAsString(
              Map.of(
                  "terminalId", terminalId.toString(),
                  "terminalRequestId", terminalRequestId.toString()));

      var request =
          HttpRequest.newBuilder()
              .uri(URI.create(functionUrl))
              .header("Content-Type", "application/json")
              .POST(HttpRequest.BodyPublishers.ofString(payload))
              .build();

      var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() >= 400) {
        logger.error(
            "Failed to release terminal reservation. Status: {}, Body: {}",
            response.statusCode(),
            response.body());
      } else {
        statsService.recordEvent(
            StatsService.CustomEvents.TERMINAL_RESERVATION_COMPENSATED,
            Map.of(
                "terminalId", terminalId.toString(),
                "terminalRequestId", terminalRequestId.toString()));
      }

    } catch (Exception e) {
      throw new TerminalReservationCompensationFailureException(
          "Unexpected error while calling Terminal Reservation Compensator Lambda", e);
    }
  }
}
