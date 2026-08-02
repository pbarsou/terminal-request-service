package com.desafio.terminalrequest.domain.events;

import com.desafio.terminalrequest.domain.entity.terminalrequest.Address;

import java.util.UUID;

public record TerminalReservationReservationConfirmed(Address address, UUID terminalId, UUID terminalRequestId) {
    public static record TerminalRequestReservationFailed(UUID terminalId, UUID terminalRequestId) {}
}
