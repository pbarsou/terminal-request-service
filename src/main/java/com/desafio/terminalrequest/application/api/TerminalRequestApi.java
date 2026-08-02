package com.desafio.terminalrequest.application.api;

import com.desafio.terminalrequest.application.api.command.CreateTerminalCommand;
import com.desafio.terminalrequest.domain.entity.terminalrequest.TerminalRequest;
import org.springframework.http.ResponseEntity;

public interface TerminalRequestApi {
    ResponseEntity<TerminalRequest> createTerminalRequest(CreateTerminalCommand request);
    ResponseEntity<TerminalRequest> findTerminalRequestById(String id);
}
