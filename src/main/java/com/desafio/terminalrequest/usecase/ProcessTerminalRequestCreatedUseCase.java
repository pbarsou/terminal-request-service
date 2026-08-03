package com.desafio.terminalrequest.usecase;

import com.desafio.terminalrequest.domain.enums.TerminalRequestsStatus;
import com.desafio.terminalrequest.domain.events.TerminalRequestCreated;
import com.desafio.terminalrequest.domain.events.TerminalRequestCustomerValidated;
import com.desafio.terminalrequest.domain.service.CustomerValidationServicePort;
import com.desafio.terminalrequest.domain.service.TerminalRequestServicePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class ProcessTerminalRequestCreatedUseCase {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final TerminalRequestServicePort terminalRequestService;
    private final CustomerValidationServicePort customerService;
    private final ApplicationEventPublisher publisher;


    public ProcessTerminalRequestCreatedUseCase(
            TerminalRequestServicePort terminalRequestService,
            CustomerValidationServicePort customerService,
            ApplicationEventPublisher publisher
    ) {
        this.terminalRequestService = terminalRequestService;
        this.customerService = customerService;
        this.publisher = publisher;
    }

    public void execute(TerminalRequestCreated event) {
        boolean isActive = customerService.isActiveCustomer(event.customerId());
        TerminalRequestsStatus status = isActive ? TerminalRequestsStatus.VALIDADO : TerminalRequestsStatus.REJEITADO;
        terminalRequestService.updateStatus(event.terminalRequestId(), status);

        if (!isActive) {
            logger.error("Request {} rejected: customer inactive or not exist", event.terminalRequestId());
            return;
        }

        logger.debug("Customer {} validated successfully", event.customerId());
        publisher.publishEvent(new TerminalRequestCustomerValidated(
                event.terminalType(),
                event.terminalRequestId(),
                event.address()
        ));
    }
}
