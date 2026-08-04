package com.desafio.terminalrequest.infrastructure.adapter.postgres.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.desafio.terminalrequest.domain.entity.terminalrequest.TerminalRequest;
import com.desafio.terminalrequest.domain.enums.TerminalRequestsStatus;
import com.desafio.terminalrequest.domain.enums.TerminalType;
import com.desafio.terminalrequest.fixtures.AddressFixture;
import com.desafio.terminalrequest.fixtures.TerminalRequestFixture;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TerminalRequestTableMappingTest {

  @Nested
  @DisplayName("Domain Mapping")
  class DomainMapping {

    @Test
    @DisplayName("should convert model to domain successfully")
    void shouldConvertModelToDomain() {
      TerminalRequestTable model = TerminalRequestFixture.createTerminalRequestTable();
      TerminalRequest domain = model.toDomain();

      assertModelToDomainEquals(model, domain);
    }

    @Test
    @DisplayName("should throw exception when converting model with null id to domain")
    void shouldThrowExceptionWhenIdIsNull() {
      TerminalRequestTable model =
          new TerminalRequestTable(
              null,
              TerminalRequestsStatus.SOLICITADO,
              "CUST-1",
              TerminalType.POS_WIFI,
              null,
              null,
              AddressFixture.createAddress(),
              Instant.now(),
              Instant.now());

      NullPointerException exception = assertThrows(NullPointerException.class, model::toDomain);

      assertEquals("Terminal Request ID must not be null", exception.getMessage());
    }

    @Test
    @DisplayName("should convert domain to model successfully")
    void shouldConvertDomainToModel() {
      TerminalRequest domain = TerminalRequestFixture.createTerminalRequest();
      TerminalRequestTable model = TerminalRequestTable.toModel(domain);

      assertModelToDomainEquals(model, domain);
    }
  }

  private void assertModelToDomainEquals(TerminalRequestTable model, TerminalRequest domain) {
    assertEquals(model.getId(), domain.getId());
    assertEquals(model.getStatus(), domain.getStatus());
    assertEquals(model.getCustomerId(), domain.getCustomerId());
    assertEquals(model.getTerminalType(), domain.getTerminalType());
    assertEquals(model.getTerminalId(), domain.getTerminalId());
    assertEquals(model.getTrackingId(), domain.getTrackingId());
    assertEquals(model.getAddress(), domain.getAddress());
    assertEquals(model.getCreatedAt(), domain.getCreatedAt());
    assertEquals(model.getUpdatedAt(), domain.getUpdatedAt());
  }
}
