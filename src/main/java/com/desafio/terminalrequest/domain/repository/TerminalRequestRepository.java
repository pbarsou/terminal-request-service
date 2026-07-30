package com.desafio.terminalrequest.domain.repository;

import com.desafio.terminalrequest.domain.entity.terminalrequest.TerminalRequest;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TerminalRequestRepository {
    TerminalRequest save(final TerminalRequest terminalRequest);
    Optional<TerminalRequest> getById(final UUID id);
}
