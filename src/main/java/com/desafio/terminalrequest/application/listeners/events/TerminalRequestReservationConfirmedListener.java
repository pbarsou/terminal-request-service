package com.desafio.terminalrequest.application.listeners.events;

import com.desafio.terminalrequest.domain.events.TerminalReservationReservationConfirmed;
import com.desafio.terminalrequest.usecase.ProcessTerminalRequestReservationConfirmedUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class TerminalRequestReservationConfirmedListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ProcessTerminalRequestReservationConfirmedUseCase processRequestReservationConfirmedUseCase;

    public TerminalRequestReservationConfirmedListener(
            ProcessTerminalRequestReservationConfirmedUseCase processRequestReservationConfirmedUseCase
    ) {
        this.processRequestReservationConfirmedUseCase = processRequestReservationConfirmedUseCase;
    }

    @Async
    @EventListener
    public void handleRequest(TerminalReservationReservationConfirmed event) {
        logger.debug("Scheduling delivery for terminal: {}", event.terminalId());
        processRequestReservationConfirmedUseCase.execute(event);
    }
}
