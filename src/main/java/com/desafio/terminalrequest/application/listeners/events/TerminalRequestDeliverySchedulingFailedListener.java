package com.desafio.terminalrequest.application.listeners.events;

import com.desafio.terminalrequest.domain.events.TerminalRequestDeliverySchedulingFailed;
import com.desafio.terminalrequest.usecase.ProcessTerminalRequestDeliverySchedulingFailedUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class TerminalRequestDeliverySchedulingFailedListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ProcessTerminalRequestDeliverySchedulingFailedUseCase processDeliverySchedulingFailedUseCase;

    public TerminalRequestDeliverySchedulingFailedListener(
            ProcessTerminalRequestDeliverySchedulingFailedUseCase processDeliverySchedulingFailedUseCase
    ) {
        this.processDeliverySchedulingFailedUseCase = processDeliverySchedulingFailedUseCase;
    }

    @Async
    @EventListener
    public void handleRequest(TerminalRequestDeliverySchedulingFailed event) {
        logger.debug("Compensating delivery scheduling failure for terminal: {}", event.terminalId());
        processDeliverySchedulingFailedUseCase.execute(event);
    }
}
