package com.desafio.terminalrequest.fixtures;

import com.desafio.terminalrequest.domain.entity.terminalrequest.TerminalRequest;
import com.desafio.terminalrequest.domain.entity.terminalrequest.TerminalRequestTable;
import com.desafio.terminalrequest.domain.enums.TerminalRequestsStatus;
import com.desafio.terminalrequest.domain.enums.TerminalType;
import java.time.Instant;
import java.util.UUID;

public class TerminalRequestFixture {

  public static TerminalRequest createTerminalRequest() {
    return new TerminalRequest("CUST-1", TerminalType.POS_WIFI, AddressFixture.createAddress());
  }

  public static TerminalRequestTable createTerminalRequestTable() {
    return new TerminalRequestTable(
        UUID.randomUUID(),
        TerminalRequestsStatus.SOLICITADO,
        "CUST-1",
        TerminalType.POS_WIFI,
        null,
        null,
        AddressFixture.createAddress(),
        Instant.now(),
        Instant.now());
  }
}
