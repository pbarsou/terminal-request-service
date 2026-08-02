package com.desafio.terminalrequest.usecase;

import com.desafio.terminalrequest.application.api.command.CreateTerminalCommand;
import com.desafio.terminalrequest.domain.entity.terminalrequest.TerminalRequest;
import com.desafio.terminalrequest.domain.enums.TerminalRequestsStatus;
import com.desafio.terminalrequest.domain.enums.TerminalType;
import com.desafio.terminalrequest.domain.exceptions.TerminalRequestNotFoundException;
import com.desafio.terminalrequest.domain.exceptions.TerminalRequestProcessException;
import com.desafio.terminalrequest.domain.service.CustomerServicePort;
import com.desafio.terminalrequest.domain.service.DeliveryServicePort;
import com.desafio.terminalrequest.domain.service.TerminalRequestServicePort;
import com.desafio.terminalrequest.domain.service.TerminalReservationServicePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Component
public class TerminalRequestUseCase {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final TerminalRequestServicePort terminalRequestService;
    private final CustomerServicePort customerService;
    private final TerminalReservationServicePort terminalReservationService;
    private final DeliveryServicePort deliveryService;

    public TerminalRequestUseCase(
            TerminalRequestServicePort terminalRequestService,
            CustomerServicePort customerService,
            TerminalReservationServicePort terminalReservationService,
            DeliveryServicePort deliveryService
    ) {
        this.terminalRequestService = terminalRequestService;
        this.customerService = customerService;
        this.terminalReservationService = terminalReservationService;
        this.deliveryService = deliveryService;
    }

    @Transactional
    public TerminalRequest execute(CreateTerminalCommand command) {
        TerminalRequest terminalRequest = new TerminalRequest(
                command.customerId(),
                command.terminalType(),
                command.address()
        );

        try {
            TerminalRequest savedRequest = terminalRequestService.insertTerminalRequest(terminalRequest);

            Optional.of(savedRequest)
                    .filter(this::validateCustomer)
                    .map(request -> processTerminalReservation(request.getTerminalType(), request.getId()))
                    .ifPresent(terminalId -> processDelivery(savedRequest, terminalId));

            return terminalRequestService.getTerminalRequestById(terminalRequest.getId())
                    .orElseThrow(() -> new TerminalRequestNotFoundException("Terminal request not found "));
        } catch (Exception exception) {
            logger.error("Failed to process terminalRequest {}", terminalRequest.getId(), exception);
            throw new TerminalRequestProcessException("Unexpected error during " +
                    "processing", terminalRequest.getId(), exception);
        }
    }

    private boolean validateCustomer(TerminalRequest request) {
        boolean isActive = customerService.isActiveCustomer(request.getCustomerId());
        TerminalRequestsStatus status = isActive ? TerminalRequestsStatus.VALIDADO : TerminalRequestsStatus.REJEITADO;

        terminalRequestService.updateStatus(request.getId(), status);

        if (!isActive) {
            logger.info("Request {} rejected: customer inactive", request.getId());
        }
        return isActive;
    }


    private UUID processTerminalReservation(TerminalType terminalType, UUID terminalRequestId) {
        logger.debug("Validating terminal availability");
        UUID terminalId = terminalReservationService.reserveATerminal(terminalType, terminalRequestId);

        if (terminalId == null) {
            terminalRequestService.updateStatus(terminalRequestId, TerminalRequestsStatus.ERRO_RESERVA);
            logger.info("Error reserving terminal for terminalRequest {}. " +
                    "No POS terminal available.", terminalRequestId);
            return null;
        }

        terminalRequestService.updateStatus(terminalRequestId, TerminalRequestsStatus.RESERVADO);
        terminalRequestService.assignTerminal(terminalRequestId, terminalId);
        logger.debug("Terminal reservation successfully, terminalId: {}", terminalId);
        return terminalId;
    }

    private void processDelivery(TerminalRequest request, UUID terminalId) {
        logger.debug("Scheduling delivery for terminal: {}", terminalId);

        UUID deliveryId = deliveryService.scheduleDelivery(request.getAddress(), request.getId(), terminalId);

        if (deliveryId == null) {
            terminalRequestService.updateStatus(request.getId(), TerminalRequestsStatus.ERRO_AGENDAMENTO);
            logger.error("Failed to schedule delivery for request {}. Terminal ID: {}", request.getId(), terminalId);
        }

        terminalRequestService.updateStatus(request.getId(), TerminalRequestsStatus.AGENDADO);
        terminalRequestService.assignTracking(request.getId(), deliveryId);
        logger.info("Delivery scheduled for request {}. Delivery ID: {}", request.getId(), deliveryId);
    }
}
