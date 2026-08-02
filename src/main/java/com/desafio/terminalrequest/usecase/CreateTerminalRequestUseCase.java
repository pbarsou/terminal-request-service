package com.desafio.terminalrequest.usecase;

import com.desafio.terminalrequest.domain.service.CustomerValidationServicePort;
import com.desafio.terminalrequest.domain.service.DeliveryServicePort;
import com.desafio.terminalrequest.domain.service.TerminalRequestServicePort;
import com.desafio.terminalrequest.domain.service.TerminalReservationServicePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateTerminalRequestUseCase {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final TerminalRequestServicePort terminalRequestService;

    public CreateTerminalRequestUseCase(
            TerminalRequestServicePort terminalRequestService
    ) {
        this.terminalRequestService = terminalRequestService;
    }
}
