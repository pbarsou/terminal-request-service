package com.desafio.terminalrequest.infrastructure.adapter;

import com.desafio.terminalrequest.domain.enums.TerminalType;
import com.desafio.terminalrequest.domain.service.TerminalReservationServicePort;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TerminalReservationStubAdapter implements TerminalReservationServicePort {

    @Override
    public UUID reserveATerminal(TerminalType terminalType, UUID terminalRequestId) {
        // Retorna um ID aleatório para simular a reserva de um terminal
        return UUID.randomUUID();
    }
}
