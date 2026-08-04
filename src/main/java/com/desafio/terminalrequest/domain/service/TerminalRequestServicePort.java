package com.desafio.terminalrequest.domain.service;

import com.desafio.terminalrequest.domain.entity.terminalrequest.TerminalRequest;
import com.desafio.terminalrequest.domain.enums.TerminalRequestsStatus;
import java.util.Optional;
import java.util.UUID;

public interface TerminalRequestServicePort {
  TerminalRequest insertTerminalRequest(TerminalRequest terminalRequest);

  Optional<TerminalRequest> getTerminalRequestById(UUID id);

  void updateStatus(UUID terminalRequestId, TerminalRequestsStatus status);

  void assignTerminal(UUID terminalRequestId, UUID terminalId);

  void assignTracking(UUID terminalRequestId, UUID trackingId);
}
