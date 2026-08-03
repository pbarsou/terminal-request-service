package com.desafio.terminalrequest.usecase;

import com.desafio.terminalrequest.application.api.command.CreateTerminalRequestCommand;
import com.desafio.terminalrequest.domain.entity.terminalrequest.TerminalRequest;
import com.desafio.terminalrequest.domain.events.TerminalRequestCreated;
import com.desafio.terminalrequest.domain.service.TerminalRequestServicePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CreateTerminalRequestUseCase {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ApplicationEventPublisher publisher;
    private final TerminalRequestServicePort terminalRequestService;

    public CreateTerminalRequestUseCase(
            TerminalRequestServicePort terminalRequestService,
            ApplicationEventPublisher publisher
    ) {
        this.terminalRequestService = terminalRequestService;
        this.publisher = publisher;
    }

    public UUID execute(CreateTerminalRequestCommand command) {
        logger.debug("Creating a new terminal request for customer: {}", command.customerId());
        TerminalRequest terminalRequest = new TerminalRequest(
                command.customerId(),
                command.terminalType(),
                command.address()
        );
        TerminalRequest savedRequest = terminalRequestService.insertTerminalRequest(terminalRequest);

        publisher.publishEvent(new TerminalRequestCreated(
                savedRequest.getCustomerId(),
                savedRequest.getId(),
                savedRequest.getTerminalType(),
                savedRequest.getAddress()
        ));
        return savedRequest.getId();
    }
}
