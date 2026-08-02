package com.desafio.terminalrequest.domain.service;

import java.util.UUID;

public interface TerminalReservationCompensatorServicePort {
    void release(UUID terminalId, UUID terminalRequestId);
}
