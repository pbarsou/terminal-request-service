package com.desafio.terminalrequest.domain.service;

import com.desafio.terminalrequest.domain.entity.terminalrequest.TerminalRequest;

import java.util.Optional;
import java.util.UUID;

public interface TerminalRequestServicePort {
    public TerminalRequest insertTerminalRequest(TerminalRequest terminalRequest);
    public Optional<TerminalRequest> getTerminalRequestById(UUID id);
}
