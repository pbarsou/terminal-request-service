package com.desafio.terminalrequest.application.api;

import com.desafio.terminalrequest.application.api.command.CreateTerminalCommand;
import com.desafio.terminalrequest.domain.entity.terminalrequest.TerminalRequest;
import com.desafio.terminalrequest.domain.service.TerminalRequestServicePort;
import com.desafio.terminalrequest.usecase.TerminalRequestUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/terminal-request")
public class TerminalRequestApiImpl implements TerminalRequestApi{

    private final TerminalRequestServicePort terminalRequestService;
    private final TerminalRequestUseCase terminalRequestUseCase;

    public TerminalRequestApiImpl(TerminalRequestServicePort terminalRequestService, TerminalRequestUseCase terminalRequestUseCase) {
        this.terminalRequestService = terminalRequestService;
        this.terminalRequestUseCase = terminalRequestUseCase;
    }

    @PostMapping
    @Override
    public ResponseEntity<TerminalRequest> createTerminalRequest(@RequestBody CreateTerminalCommand request) {
        terminalRequestUseCase.execute(request);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<TerminalRequest> findTerminalRequestById(@PathVariable String id) {
        return terminalRequestService.getTerminalRequestById(UUID.fromString(id))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
