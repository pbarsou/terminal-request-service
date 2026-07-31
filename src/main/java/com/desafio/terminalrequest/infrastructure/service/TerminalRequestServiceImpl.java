package com.desafio.terminalrequest.infrastructure.service;

import com.desafio.terminalrequest.domain.entity.terminalrequest.TerminalRequest;
import com.desafio.terminalrequest.domain.repository.TerminalRequestRepository;
import com.desafio.terminalrequest.domain.service.TerminalRequestServicePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TerminalRequestServiceImpl implements TerminalRequestServicePort {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final TerminalRequestRepository repository;

    public TerminalRequestServiceImpl(TerminalRequestRepository repository) {
        this.repository = repository;
    }

    @Override
    public TerminalRequest insertTerminalRequest(TerminalRequest terminalRequest) {
        logger.debug("Saving a new terminal request");
        return repository.save(terminalRequest);
    }

    @Override
    public Optional<TerminalRequest> getTerminalRequestById(UUID id) {
        logger.debug("Fetching terminal request by ID: {}", id);
        return repository.getById(id);
    }
}
