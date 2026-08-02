package com.desafio.terminalrequest.infrastructure.adapter;

import com.desafio.terminalrequest.domain.service.CustomerServicePort;
import com.desafio.terminalrequest.infrastructure.adapter.response.LambdaCustomerValidationReponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.lambda.model.LambdaResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@Component
@ConditionalOnProperty(name = "aws.lambda.enabled", havingValue = "true")
public class LambdaCustomerValidationAdapter implements CustomerServicePort {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String functionUrl;

    public LambdaCustomerValidationAdapter(ObjectMapper objectMapper,
                                           @Value("${aws.lambda.customer-validation-function}") String functionUrl) {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = objectMapper;
        this.functionUrl = functionUrl;
    }

    @Override
    public boolean isActiveCustomer(String customerId) {
        try {
            var payload = objectMapper.writeValueAsString(Map.of("customerId", customerId));

            var request = HttpRequest.newBuilder()
                    .uri(URI.create(functionUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payload))
                    .build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return objectMapper.readValue(response.body(), LambdaCustomerValidationReponse.class).active();

        } catch (Exception e) {
            throw new IllegalStateException("Falha na validação do cliente", e);
        }
    }
}