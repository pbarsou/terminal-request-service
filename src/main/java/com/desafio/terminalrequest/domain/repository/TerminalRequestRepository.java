package com.desafio.terminalrequest.domain.repository;

import com.desafio.terminalrequest.domain.entity.terminalrequest.TerminalRequest;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface TerminalRequestRepository {
  TerminalRequest save(final TerminalRequest terminalRequest);

  Optional<TerminalRequest> getById(final UUID id);
}
