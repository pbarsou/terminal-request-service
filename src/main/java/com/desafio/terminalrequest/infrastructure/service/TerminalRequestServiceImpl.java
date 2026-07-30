package com.desafio.terminalrequest.infrastructure.service;

import com.desafio.terminalrequest.domain.entity.terminalrequest.TerminalRequest;
import com.desafio.terminalrequest.domain.repository.TerminalRequestRepository;
import com.desafio.terminalrequest.domain.service.TerminalRequestServicePort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TerminalRequestServiceImpl implements TerminalRequestServicePort {

    private TerminalRequestRepository repository;

    public TerminalRequestServiceImpl(TerminalRequestRepository repository) {
        this.repository = repository;
    }

    @Override
    public TerminalRequest insertTerminalRequest(TerminalRequest terminalRequest) {
        return repository.save(terminalRequest);
    }

    @Override
    public Optional<TerminalRequest> getTerminalRequestById(UUID id) {
        return repository.getById(id);
    }
}
