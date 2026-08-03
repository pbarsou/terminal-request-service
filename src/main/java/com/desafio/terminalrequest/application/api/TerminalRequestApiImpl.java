package com.desafio.terminalrequest.application.api;

import com.desafio.terminalrequest.application.api.command.CreateTerminalRequestCommand;
import com.desafio.terminalrequest.domain.entity.terminalrequest.TerminalRequest;
import com.desafio.terminalrequest.domain.service.TerminalRequestServicePort;
import com.desafio.terminalrequest.usecase.CreateTerminalRequestUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/terminal-request")
public class TerminalRequestApiImpl implements TerminalRequestApi{

    private final TerminalRequestServicePort terminalRequestService;
    private final CreateTerminalRequestUseCase createTerminalRequestUseCase;

    public TerminalRequestApiImpl(
            TerminalRequestServicePort terminalRequestService,
            CreateTerminalRequestUseCase createTerminalRequestUseCase
    ) {
        this.terminalRequestService = terminalRequestService;
        this.createTerminalRequestUseCase = createTerminalRequestUseCase;
    }

    @PostMapping
    @Override
    public ResponseEntity<UUID> createTerminalRequest(@RequestBody CreateTerminalRequestCommand request) {
        return ResponseEntity.ok().body(createTerminalRequestUseCase.execute(request));
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<TerminalRequest> findTerminalRequestById(@PathVariable String id) {
        return terminalRequestService.getTerminalRequestById(UUID.fromString(id))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
