package com.desafio.terminalrequest.domain.service;

import com.desafio.terminalrequest.domain.enums.TerminalType;

import java.util.UUID;

public interface TerminalReservationServicePort {
    UUID reserveATerminal(TerminalType terminalType, UUID terminalRequestId);
}
