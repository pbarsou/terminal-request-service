package com.desafio.terminalrequest.infrastructure.adapter.lambda;

import com.desafio.terminalrequest.domain.exceptions.CustomerValidationFailureException;
import com.desafio.terminalrequest.domain.service.CustomerValidationServicePort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@Component
public class LambdaCustomerValidationServiceAdapter implements CustomerValidationServicePort {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String functionUrl;

    public LambdaCustomerValidationServiceAdapter(HttpClient httpClient,
                                                  ObjectMapper objectMapper,
                                                  @Value("${aws.lambda.customer-validation-function}") String functionUrl) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
        this.functionUrl = functionUrl.trim();
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

            return objectMapper.readValue(response.body(), LambdaCustomerValidationResponse.class).active();

        } catch (Exception exception) {
            throw new CustomerValidationFailureException("Customer validation failure", exception);
        }
    }

    public record LambdaCustomerValidationResponse(Boolean active) { }
}