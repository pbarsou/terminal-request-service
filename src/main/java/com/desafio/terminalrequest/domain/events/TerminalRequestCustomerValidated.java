package com.desafio.terminalrequest.domain.events;

import com.desafio.terminalrequest.domain.entity.terminalrequest.Address;
import com.desafio.terminalrequest.domain.enums.TerminalType;
import java.util.UUID;

public record TerminalRequestCustomerValidated(
    TerminalType terminalType, UUID terminalRequestId, Address address) {}
