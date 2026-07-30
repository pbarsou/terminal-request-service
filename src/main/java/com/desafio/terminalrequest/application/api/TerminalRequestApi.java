package com.desafio.terminalrequest.application.api;

import com.desafio.terminalrequest.application.api.request.CreateTerminalRequest;
import com.desafio.terminalrequest.domain.entity.terminalrequest.TerminalRequest;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface TerminalRequestApi {
    public ResponseEntity<TerminalRequest> createTerminalRequest(CreateTerminalRequest request);
    public ResponseEntity<TerminalRequest> findTerminalRequestById(String id);
}
