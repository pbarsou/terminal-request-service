package com.desafio.terminalrequest.application.api;

import com.desafio.terminalrequest.application.api.request.CreateTerminalRequest;
import com.desafio.terminalrequest.domain.entity.terminalrequest.TerminalRequest;
import com.desafio.terminalrequest.domain.service.TerminalRequestServicePort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/terminal-request")
public class TerminalRequestApiImpl implements TerminalRequestApi{

    private TerminalRequestServicePort terminalRequestService;

    public TerminalRequestApiImpl(TerminalRequestServicePort terminalRequestService) {
        this.terminalRequestService = terminalRequestService;
    }

    @PostMapping
    @Override
    public ResponseEntity<TerminalRequest> createTerminalRequest(@RequestBody CreateTerminalRequest request) {
        return ResponseEntity.ok().body(terminalRequestService.insertTerminalRequest(new TerminalRequest(
                request.customerId(),
                request.terminalType(),
                request.address()
        )));
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<TerminalRequest> findTerminalRequestById(@PathVariable String id) {
        return terminalRequestService.getTerminalRequestById(UUID.fromString(id))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
