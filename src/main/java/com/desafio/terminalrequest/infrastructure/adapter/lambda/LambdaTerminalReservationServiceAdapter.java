package com.desafio.terminalrequest.infrastructure.adapter.lambda;

import com.desafio.terminalrequest.domain.enums.TerminalType;
import com.desafio.terminalrequest.domain.exceptions.TerminalReservationFailureException;
import com.desafio.terminalrequest.domain.service.TerminalReservationServicePort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.UUID;

@Component
public class LambdaTerminalReservationServiceAdapter implements TerminalReservationServicePort {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String functionUrl;

    public LambdaTerminalReservationServiceAdapter(HttpClient httpClient,
                                                   ObjectMapper objectMapper,
                                                   @Value("${aws.lambda.terminal-reservation-function}") String functionUrl) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
        this.functionUrl = functionUrl.trim();
    }

    @Override
    public UUID reserveATerminal(TerminalType terminalType, UUID terminalRequestId) {
        try {
            var payload = objectMapper.writeValueAsString(Map.of(
                    "terminalType", terminalType.name(),
                    "requestId", terminalRequestId.toString()
            ));

            var request = HttpRequest.newBuilder()
                    .uri(URI.create(functionUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payload))
                    .build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                return null;
            }

            return objectMapper.readValue(response.body(), LambdaTerminalReservationResponse.class).terminalId();

        } catch (Exception exception) {
            throw new TerminalReservationFailureException("Failed to process terminal reservation", exception);
        }
    }

    public record LambdaTerminalReservationResponse(UUID terminalId) {}
}
