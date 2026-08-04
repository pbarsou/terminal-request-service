package com.desafio.terminalrequest.domain.entity.terminalrequest;

import com.desafio.terminalrequest.domain.enums.TerminalRequestsStatus;
import com.desafio.terminalrequest.domain.enums.TerminalType;
import com.desafio.terminalrequest.fixtures.AddressFixture;
import com.desafio.terminalrequest.fixtures.TerminalRequestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TerminalRequestTest {

    @Test
    @DisplayName("should create terminal request with default values")
    void shouldCreateTerminalRequestWithDefaultValues() {
        TerminalRequest request = TerminalRequestFixture.createTerminalRequest();

        assertThat(request.getId()).isNotNull();
        assertThat(request.getStatus()).isEqualTo(TerminalRequestsStatus.SOLICITADO);
        assertThat(request.getCustomerId()).isEqualTo("CUST-1");
        assertThat(request.getTerminalType()).isEqualTo(TerminalType.POS_WIFI);
        assertThat(request.getCreatedAt()).isNotNull();
        assertThat(request.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("should test getters and setters")
    void shouldTestGettersAndSetters() {
        TerminalRequest request = TerminalRequestFixture.createTerminalRequest();
        UUID terminalId = UUID.randomUUID();
        UUID trackingId = UUID.randomUUID();
        Instant now = Instant.now();

        request.setStatus(TerminalRequestsStatus.RESERVADO);
        request.setTerminalId(terminalId);
        request.setTrackingId(trackingId);
        request.setUpdatedAt(now);

        assertEquals(TerminalRequestsStatus.RESERVADO, request.getStatus());
        assertEquals(terminalId, request.getTerminalId());
        assertEquals(trackingId, request.getTrackingId());
        assertEquals(now, request.getUpdatedAt());
    }

    @Nested
    @DisplayName("Equality and HashCode")
    class EqualityAndHashCode {

        @Test
        @DisplayName("should test equals and hashCode")
        void shouldTestEqualsAndHashCode() {
            UUID id = UUID.randomUUID();
            Address address = AddressFixture.createAddress();
            Instant now = Instant.now();

            TerminalRequest request1 = new TerminalRequest(
                    id,
                    TerminalRequestsStatus.SOLICITADO,
                    "CUST-1",
                    TerminalType.POS_WIFI,
                    null,
                    null,
                    address,
                    now,
                    now
            );

            TerminalRequest request2 = new TerminalRequest(
                    id,
                    TerminalRequestsStatus.SOLICITADO,
                    "CUST-1",
                    TerminalType.POS_WIFI,
                    null,
                    null,
                    address,
                    now,
                    now
            );

            TerminalRequest request3 = new TerminalRequest(
                    UUID.randomUUID(),
                    TerminalRequestsStatus.SOLICITADO,
                    "CUST-1",
                    TerminalType.POS_WIFI,
                    null,
                    null,
                    address,
                    now,
                    now
            );

            assertEquals(request1, request2);
            assertEquals(request1.hashCode(), request2.hashCode());
            assertEquals(request1, request1);
            assertNotEquals(request1, request3);
            assertNotEquals(request1, null);
            assertNotEquals(request1, new Object());
        }

        @Test
        @DisplayName("should return false when any field is different")
        void shouldReturnFalseWhenFieldsAreDifferent() {
            UUID id = UUID.randomUUID();
            Address address = AddressFixture.createAddress();
            Instant now = Instant.now();
            TerminalRequest base = new TerminalRequest(id, TerminalRequestsStatus.SOLICITADO, "C1", TerminalType.POS_WIFI, null, null, address, now, now);

            assertNotEquals(base, new TerminalRequest(UUID.randomUUID(), TerminalRequestsStatus.SOLICITADO, "C1", TerminalType.POS_WIFI, null, null, address, now, now));

            TerminalRequest diffStatus = new TerminalRequest(id, TerminalRequestsStatus.AGENDADO, "C1", TerminalType.POS_WIFI, null, null, address, now, now);
            assertNotEquals(base, diffStatus);

            TerminalRequest diffCustomer = new TerminalRequest(id, TerminalRequestsStatus.SOLICITADO, "C2", TerminalType.POS_WIFI, null, null, address, now, now);
            assertNotEquals(base, diffCustomer);

            TerminalRequest diffType = new TerminalRequest(id, TerminalRequestsStatus.SOLICITADO, "C1", TerminalType.POS_5G, null, null, address, now, now);
            assertNotEquals(base, diffType);

            TerminalRequest diffAddress = new TerminalRequest(id, TerminalRequestsStatus.SOLICITADO, "C1", TerminalType.POS_WIFI, null, null, new Address("R", "1", "C", "S", "Z"), now, now);
            assertNotEquals(base, diffAddress);

            TerminalRequest diffTerminal = new TerminalRequest(id, TerminalRequestsStatus.SOLICITADO, "C1", TerminalType.POS_WIFI, UUID.randomUUID(), null, address, now, now);
            assertNotEquals(base, diffTerminal);

            TerminalRequest diffTracking = new TerminalRequest(id, TerminalRequestsStatus.SOLICITADO, "C1", TerminalType.POS_WIFI, null, UUID.randomUUID(), address, now, now);
            assertNotEquals(base, diffTracking);

            TerminalRequest diffCreated = new TerminalRequest(id, TerminalRequestsStatus.SOLICITADO, "C1", TerminalType.POS_WIFI, null, null, address, now.plusSeconds(1), now);
            assertNotEquals(base, diffCreated);

            TerminalRequest diffUpdated = new TerminalRequest(id, TerminalRequestsStatus.SOLICITADO, "C1", TerminalType.POS_WIFI, null, null, address, now, now.plusSeconds(1));
            assertNotEquals(base, diffUpdated);
        }
    }
}
