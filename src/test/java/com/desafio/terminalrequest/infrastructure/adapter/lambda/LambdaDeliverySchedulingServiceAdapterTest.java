package com.desafio.terminalrequest.infrastructure.adapter.lambda;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.desafio.terminalrequest.domain.exceptions.DeliverySchedulingException;
import com.desafio.terminalrequest.fixtures.AddressFixture;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LambdaDeliverySchedulingServiceAdapterTest {

  @Mock private HttpClient httpClient;

  @Mock private HttpResponse<Object> httpResponse;

  private final ObjectMapper objectMapper = new ObjectMapper();
  private LambdaDeliverySchedulingServiceAdapter adapter;

  @BeforeEach
  void setUp() {
    String functionUrl = "http://localhost:4566/functions/delivery";
    adapter = new LambdaDeliverySchedulingServiceAdapter(httpClient, objectMapper, functionUrl);
  }

  @Test
  @DisplayName("Should return tracking ID when scheduling successfully")
  void shouldReturnTrackingIdWhenSchedulingIsSuccessful() throws IOException, InterruptedException {
    UUID requestId = UUID.randomUUID();
    UUID terminalId = UUID.randomUUID();
    UUID trackingId = UUID.randomUUID();
    String responseBody = "{\"trackingId\": \"" + trackingId + "\"}";

    when(httpResponse.statusCode()).thenReturn(200);
    when(httpResponse.body()).thenReturn(responseBody);
    when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
        .thenReturn(httpResponse);

    UUID result = adapter.scheduleDelivery(AddressFixture.createAddress(), requestId, terminalId);

    assertThat(result).isEqualTo(trackingId);
  }

  @Test
  @DisplayName("Should return null when Lambda function returns error")
  void shouldReturnNullWhenLambdaReturnsError() throws IOException, InterruptedException {
    when(httpResponse.statusCode()).thenReturn(400);
    when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
        .thenReturn(httpResponse);

    UUID result =
        adapter.scheduleDelivery(
            AddressFixture.createAddress(), UUID.randomUUID(), UUID.randomUUID());

    assertThat(result).isNull();
  }

  @Test
  @DisplayName("Should throw exception when Lambda function call fails")
  void shouldThrowExceptionWhenLambdaCallFails() throws IOException, InterruptedException {
    when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
        .thenThrow(new IOException("Connection error"));

    assertThatThrownBy(
            () ->
                adapter.scheduleDelivery(
                    AddressFixture.createAddress(), UUID.randomUUID(), UUID.randomUUID()))
        .isInstanceOf(DeliverySchedulingException.class)
        .hasMessage("Failed to schedule delivery");
  }
}
