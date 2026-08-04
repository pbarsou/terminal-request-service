package com.desafio.terminalrequest.infrastructure.adapter.lambda;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.desafio.terminalrequest.domain.exceptions.TerminalReservationCompensationFailureException;
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
class LambdaTerminalReservationCompensatorServiceAdapterTest {

  @Mock private HttpClient httpClient;

  @Mock private HttpResponse<Object> httpResponse;

  private final ObjectMapper objectMapper = new ObjectMapper();
  private LambdaTerminalReservationCompensatorServiceAdapter adapter;

  @BeforeEach
  void setUp() {
    String functionUrl = "http://localhost:4566/functions/compensator";
    adapter =
        new LambdaTerminalReservationCompensatorServiceAdapter(
            httpClient, objectMapper, functionUrl);
  }

  @Test
  @DisplayName("Should call Lambda function successfully")
  void shouldCallLambdaSuccessfully() throws IOException, InterruptedException {
    when(httpResponse.statusCode()).thenReturn(200);
    when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
        .thenReturn(httpResponse);

    adapter.release(UUID.randomUUID(), UUID.randomUUID());

    verify(httpClient).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
  }

  @Test
  @DisplayName("Should handle Lambda function error")
  void shouldHandleLambdaError() throws IOException, InterruptedException {
    when(httpResponse.statusCode()).thenReturn(500);
    when(httpResponse.body()).thenReturn("Internal error");
    when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
        .thenReturn(httpResponse);

    adapter.release(UUID.randomUUID(), UUID.randomUUID());

    verify(httpClient).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
  }

  @Test
  @DisplayName("Should throw exception when Lambda function call fails")
  void shouldHandleExceptionDuringCall() throws IOException, InterruptedException {
    when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
        .thenThrow(new IOException("Connection error"));

    assertThatThrownBy(() -> adapter.release(UUID.randomUUID(), UUID.randomUUID()))
        .isInstanceOf(TerminalReservationCompensationFailureException.class)
        .hasMessage("Unexpected error while calling Terminal Reservation Compensator Lambda");

    verify(httpClient).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
  }
}
