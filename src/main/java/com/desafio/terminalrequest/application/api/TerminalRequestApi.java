package com.desafio.terminalrequest.application.api;

import com.desafio.terminalrequest.application.api.command.CreateTerminalRequestCommand;
import com.desafio.terminalrequest.domain.entity.terminalrequest.TerminalRequest;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

public interface TerminalRequestApi {
  ResponseEntity<UUID> createTerminalRequest(CreateTerminalRequestCommand request);

  ResponseEntity<TerminalRequest> findTerminalRequestById(UUID id);
}
