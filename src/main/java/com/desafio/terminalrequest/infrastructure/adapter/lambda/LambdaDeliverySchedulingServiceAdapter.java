package com.desafio.terminalrequest.infrastructure.adapter.lambda;

import com.desafio.terminalrequest.domain.entity.terminalrequest.Address;
import com.desafio.terminalrequest.domain.exceptions.DeliverySchedulingException;
import com.desafio.terminalrequest.domain.service.DeliverySchedulingServicePort;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LambdaDeliverySchedulingServiceAdapter implements DeliverySchedulingServicePort {

  private final HttpClient httpClient;
  private final ObjectMapper objectMapper;
  private final String functionUrl;

  public LambdaDeliverySchedulingServiceAdapter(
      HttpClient httpClient,
      ObjectMapper objectMapper,
      @Value("${aws.lambda.delivery-function}") String functionUrl) {
    this.httpClient = httpClient;
    this.objectMapper = objectMapper;
    this.functionUrl = functionUrl.trim();
  }

  @Override
  public UUID scheduleDelivery(Address address, UUID terminalRequestId, UUID terminalId) {
    try {
      var payload =
          objectMapper.writeValueAsString(
              Map.of(
                  "address", address,
                  "requestId", terminalRequestId.toString(),
                  "terminalId", terminalId.toString()));

      var request =
          HttpRequest.newBuilder()
              .uri(URI.create(functionUrl))
              .header("Content-Type", "application/json")
              .POST(HttpRequest.BodyPublishers.ofString(payload))
              .build();

      var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() >= 400) {
        return null;
      }

      return objectMapper.readValue(response.body(), LambdaDeliveryResponse.class).trackingId();

    } catch (Exception exception) {
      throw new DeliverySchedulingException("Failed to schedule delivery", exception);
    }
  }

  public record LambdaDeliveryResponse(UUID trackingId) {}
}
