package com.desafio.terminalrequest.infrastructure.adapter;

import com.desafio.terminalrequest.domain.enums.TerminalType;
import com.desafio.terminalrequest.domain.service.TerminalReservationServicePort;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@ConditionalOnProperty(name = "aws.lambda.enabled", havingValue = "false", matchIfMissing = true)
public class LocalTerminalReservationServiceAdapter implements TerminalReservationServicePort {

    @Override
    public UUID reserveATerminal(TerminalType terminalType, UUID terminalRequestId) {
        return UUID.randomUUID();
    }
}
