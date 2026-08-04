package com.desafio.terminalrequest.integrated;

import com.desafio.terminalrequest.application.api.command.CreateTerminalRequestCommand;
import com.desafio.terminalrequest.domain.enums.TerminalRequestsStatus;
import com.desafio.terminalrequest.fixtures.TerminalRequestCommandFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CreateTerminalRequestIT extends IntegrationTest {

    private static final String BASE_URL = "/terminal-request";

    @Nested
    @DisplayName("Create Terminal Request")
    class CreateRequest {

        @Test
        @DisplayName("POST /terminal-request should return 200 OK and create the request")
        void shouldReturn200WhenCreated() {
            CreateTerminalRequestCommand command = TerminalRequestCommandFixture.createCommand();

            ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL, command, String.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            String body = response.getBody().replace("\"", "");
            UUID.fromString(body);
        }
    }

    @Nested
    @DisplayName("Get Terminal Request")
    class GetRequest {

        @Test
        @DisplayName("GET /terminal-request/{id} should return 200 OK with request details")
        void shouldReturn200WhenFound() {
            CreateTerminalRequestCommand command = TerminalRequestCommandFixture.createCommand();
            ResponseEntity<String> createResponse = restTemplate.postForEntity(BASE_URL, command, String.class);
            assertEquals(HttpStatus.OK, createResponse.getStatusCode());
            assertNotNull(createResponse.getBody());
            String terminalRequestId = createResponse.getBody().replace("\"", "");

            ResponseEntity<Map> response = restTemplate.getForEntity(BASE_URL + "/" + terminalRequestId, Map.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            Map<String, Object> body = response.getBody();
            assertNotNull(body);
            assertEquals(terminalRequestId, body.get("id"));
            assertEquals(command.customerId(), body.get("customerId"));
            assertEquals(TerminalRequestsStatus.SOLICITADO.name(), body.get("status"));
        }

        @Test
        @DisplayName("GET /terminal-request/{id} should return 404 when request does not exist")
        void shouldReturn404WhenNotFound() {
            UUID nonExistentId = UUID.randomUUID();

            ResponseEntity<Void> response = restTemplate.getForEntity(BASE_URL + "/" + nonExistentId, Void.class);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }
}
