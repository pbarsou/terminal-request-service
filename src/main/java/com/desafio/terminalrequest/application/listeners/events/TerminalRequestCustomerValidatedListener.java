package com.desafio.terminalrequest.application.listeners.events;

import com.desafio.terminalrequest.domain.events.TerminalRequestCustomerValidated;
import com.desafio.terminalrequest.usecase.ProcessTerminalRequestCustomerValidatedUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class TerminalRequestCustomerValidatedListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ProcessTerminalRequestCustomerValidatedUseCase processCustomerValidatedUseCase;


    public TerminalRequestCustomerValidatedListener(
            ProcessTerminalRequestCustomerValidatedUseCase processCustomerValidatedUseCase
    ) {
        this.processCustomerValidatedUseCase = processCustomerValidatedUseCase;
    }

    @Async
    @EventListener
    public void handleRequest(TerminalRequestCustomerValidated event) {
        logger.debug("Validating terminal availability");
        processCustomerValidatedUseCase.execute(event);
    }
}
