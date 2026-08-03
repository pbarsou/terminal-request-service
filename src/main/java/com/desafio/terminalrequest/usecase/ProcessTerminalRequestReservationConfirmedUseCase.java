package com.desafio.terminalrequest.usecase;

import com.desafio.terminalrequest.domain.enums.TerminalRequestsStatus;
import com.desafio.terminalrequest.domain.events.TerminalRequestDeliverySchedulingFailed;
import com.desafio.terminalrequest.domain.events.TerminalReservationReservationConfirmed;
import com.desafio.terminalrequest.domain.service.DeliverySchedulingServicePort;
import com.desafio.terminalrequest.domain.service.TerminalRequestServicePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProcessTerminalRequestReservationConfirmedUseCase {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final TerminalRequestServicePort terminalRequestService;
    private final DeliverySchedulingServicePort deliveryService;
    private final ApplicationEventPublisher publisher;


    public ProcessTerminalRequestReservationConfirmedUseCase(
            TerminalRequestServicePort terminalRequestService,
            DeliverySchedulingServicePort deliveryService,
            ApplicationEventPublisher publisher
            ) {
        this.terminalRequestService = terminalRequestService;
        this.deliveryService = deliveryService;
        this.publisher = publisher;
    }

    public void execute (TerminalReservationReservationConfirmed event) {
        logger.debug("Scheduling delivery for terminal: {}", event.terminalId());

        UUID trackingId = deliveryService.scheduleDelivery(event.address(), event.terminalRequestId(), event.terminalId());

        if (trackingId == null) {
            terminalRequestService.updateStatus(event.terminalRequestId(), TerminalRequestsStatus.ERRO_AGENDAMENTO);

            logger.error("Failed to schedule delivery for request {}. " +
                    "Terminal ID: {}", event.terminalRequestId(), event.terminalId());

            publisher.publishEvent(new TerminalRequestDeliverySchedulingFailed(
                    event.terminalId(),
                    event.terminalRequestId()
            ));
            return;
        }

        terminalRequestService.updateStatus(event.terminalRequestId(), TerminalRequestsStatus.AGENDADO);
        terminalRequestService.assignTracking(event.terminalRequestId(), trackingId);
        logger.info("Delivery scheduled for request {}. Tracking ID: {}", event.terminalRequestId(), trackingId);
    }
}
