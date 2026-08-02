package com.desafio.terminalrequest.infrastructure.adapter;

import com.desafio.terminalrequest.domain.service.TerminalReservationCompensatorServicePort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.UUID;

@Component
@ConditionalOnProperty(name = "aws.lambda.enabled", havingValue = "true")
public class LambdaTerminalReservationCompensatorServiceAdapter implements TerminalReservationCompensatorServicePort {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String functionUrl;

    public LambdaTerminalReservationCompensatorServiceAdapter(
            ObjectMapper objectMapper,
            @Value("${aws.lambda.terminal-reservation-compensator-function}") String functionUrl
    ) {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = objectMapper;
        this.functionUrl = functionUrl.trim();
    }

    @Override
    public void release(UUID terminalId, UUID terminalRequestId) {
        try {
            var payload = objectMapper.writeValueAsString(Map.of(
                    "terminalId", terminalId.toString(),
                    "terminalRequestId", terminalRequestId.toString()
            ));

            var request = HttpRequest.newBuilder()
                    .uri(URI.create(functionUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payload))
                    .build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                logger.error("Failed to release terminal reservation via Lambda. Status: {}, Body: {}", 
                        response.statusCode(), response.body());
            } else {
                logger.info("Terminal reservation release request sent successfully for terminalId: {}", terminalId);
            }

        } catch (Exception e) {
            logger.error("Unexpected error while calling Terminal Reservation Compensator Lambda", e);
        }
    }
}
