package com.desafio.terminalrequest.usecase;

import com.desafio.terminalrequest.domain.enums.TerminalRequestsStatus;
import com.desafio.terminalrequest.domain.events.TerminalRequestCustomerValidated;
import com.desafio.terminalrequest.domain.events.TerminalReservationReservationConfirmed;
import com.desafio.terminalrequest.domain.service.TerminalRequestServicePort;
import com.desafio.terminalrequest.domain.service.TerminalReservationServicePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class ProcessTerminalRequestCustomerValidatedUseCase {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ApplicationEventPublisher publisher;
    private final TerminalRequestServicePort terminalRequestService;
    private final TerminalReservationServicePort terminalReservationService;

    public ProcessTerminalRequestCustomerValidatedUseCase(
            ApplicationEventPublisher publisher,
            TerminalRequestServicePort terminalRequestService,
            TerminalReservationServicePort terminalReservationService
    ) {
        this.publisher = publisher;
        this.terminalRequestService = terminalRequestService;
        this.terminalReservationService = terminalReservationService;
    }

    public void execute(TerminalRequestCustomerValidated event) {
        UUID terminalId = terminalReservationService.reserveATerminal(event.terminalType(), event.terminalRequestId());

        if (terminalId == null) {
            terminalRequestService.updateStatus(event.terminalRequestId(), TerminalRequestsStatus.ERRO_RESERVA);
            logger.error("Error reserving terminal for terminalRequest {}. " +
                    "No POS terminal available.", event.terminalRequestId());
            return;
        }

        terminalRequestService.updateStatus(event.terminalRequestId(), TerminalRequestsStatus.RESERVADO);
        terminalRequestService.assignTerminal(event.terminalRequestId(), terminalId);
        logger.debug("Terminal reservation successfully, terminalId: {}", terminalId);

        publisher.publishEvent(new TerminalReservationReservationConfirmed(
                event.address(),
                terminalId,
                event.terminalRequestId()
        ));
    }
}
