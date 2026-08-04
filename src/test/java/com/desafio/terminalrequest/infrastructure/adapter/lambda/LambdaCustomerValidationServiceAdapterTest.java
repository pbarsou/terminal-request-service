package com.desafio.terminalrequest.infrastructure.adapter.lambda;

import com.desafio.terminalrequest.domain.exceptions.CustomerValidationFailureException;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LambdaCustomerValidationServiceAdapterTest {

    @Mock
    private HttpClient httpClient;

    @Mock
    private HttpResponse<Object> httpResponse;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private LambdaCustomerValidationServiceAdapter adapter;

    @BeforeEach
    void setUp() {
        String functionUrl = "http://localhost:4566/functions/customer-validation";
        adapter = new LambdaCustomerValidationServiceAdapter(httpClient, objectMapper, functionUrl);
    }

    @Test
    @DisplayName("Should return true when customer is active")
    void shouldReturnTrueWhenCustomerIsActive() throws IOException, InterruptedException {
        String customerId = "CUST-1";
        String responseBody = "{\"active\": true}";
        when(httpResponse.body()).thenReturn(responseBody);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);

        boolean result = adapter.isActiveCustomer(customerId);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should return false when customer is inactive")
    void shouldReturnFalseWhenCustomerIsInactive() throws IOException, InterruptedException {
        String customerId = "CUST-1";
        String responseBody = "{\"active\": false}";
        when(httpResponse.body()).thenReturn(responseBody);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);

        boolean result = adapter.isActiveCustomer(customerId);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should throw exception when Lambda function call fails")
    void shouldThrowExceptionWhenLambdaCallFails() throws IOException, InterruptedException {
        String customerId = "CUST-1";
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenThrow(new IOException("Connection failed"));

        assertThatThrownBy(() -> adapter.isActiveCustomer(customerId))
                .isInstanceOf(CustomerValidationFailureException.class)
                .hasMessage("Customer validation failure");
    }
}
