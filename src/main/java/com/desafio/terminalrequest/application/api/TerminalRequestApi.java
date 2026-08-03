package com.desafio.terminalrequest.application.api;

import com.desafio.terminalrequest.application.api.command.CreateTerminalRequestCommand;
import com.desafio.terminalrequest.domain.entity.terminalrequest.TerminalRequest;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface TerminalRequestApi {
    ResponseEntity<UUID> createTerminalRequest(CreateTerminalRequestCommand request);
    ResponseEntity<TerminalRequest> findTerminalRequestById(String id);
}
