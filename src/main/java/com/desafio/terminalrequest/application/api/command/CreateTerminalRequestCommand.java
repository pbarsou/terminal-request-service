package com.desafio.terminalrequest.application.api.command;

import com.desafio.terminalrequest.domain.entity.terminalrequest.Address;
import com.desafio.terminalrequest.domain.enums.TerminalType;

public record CreateTerminalRequestCommand(
    String customerId,
    TerminalType terminalType,
    Address address
) {}
