package com.desafio.terminalrequest.fixtures;

import com.desafio.terminalrequest.application.api.command.CreateTerminalRequestCommand;
import com.desafio.terminalrequest.domain.enums.TerminalType;

public class TerminalRequestCommandFixture {
  public static CreateTerminalRequestCommand createCommand() {
    return new CreateTerminalRequestCommand(
        "CUST-1", TerminalType.POS_WIFI, AddressFixture.createAddress());
  }
}
