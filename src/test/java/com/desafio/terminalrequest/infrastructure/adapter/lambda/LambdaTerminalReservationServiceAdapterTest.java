package com.desafio.terminalrequest.infrastructure.adapter.lambda;

import com.desafio.terminalrequest.domain.enums.TerminalType;
import com.desafio.terminalrequest.domain.exceptions.TerminalReservationFailureException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LambdaTerminalReservationServiceAdapterTest {

    @Mock
    private HttpClient httpClient;

    @Mock
    private HttpResponse<Object> httpResponse;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private LambdaTerminalReservationServiceAdapter adapter;

    @BeforeEach
    void setUp() {
        String functionUrl = "http://localhost:4566/functions/reservation";
        adapter = new LambdaTerminalReservationServiceAdapter(httpClient, objectMapper, functionUrl);
    }

    @Test
    @DisplayName("Should return terminal ID when reservation successfully")
    void shouldReturnTerminalIdWhenReservationIsSuccessful() throws IOException, InterruptedException {
        UUID terminalId = UUID.randomUUID();
        String responseBody = "{\"terminalId\": \"" + terminalId + "\"}";

        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.body()).thenReturn(responseBody);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);

        UUID result = adapter.reserveATerminal(TerminalType.POS_WIFI, UUID.randomUUID());

        assertThat(result).isEqualTo(terminalId);
    }

    @Test
    @DisplayName("Should return null when Lambda function returns error")
    void shouldReturnNullWhenLambdaReturnsError() throws IOException, InterruptedException {
        when(httpResponse.statusCode()).thenReturn(400);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);

        UUID result = adapter.reserveATerminal(TerminalType.POS_WIFI, UUID.randomUUID());

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should throw exception when Lambda function call fails")
    void shouldThrowExceptionWhenLambdaCallFails() throws IOException, InterruptedException {
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenThrow(new IOException("Connection error"));

        assertThatThrownBy(() -> adapter.reserveATerminal(TerminalType.POS_WIFI, UUID.randomUUID()))
                .isInstanceOf(TerminalReservationFailureException.class)
                .hasMessage("Failed to process terminal reservation");
    }
}
