package com.desafio.terminalrequest.infrastructure.adapter;

import com.desafio.terminalrequest.domain.service.TerminalReservationCompensatorServicePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@ConditionalOnProperty(name = "aws.lambda.enabled", havingValue = "false", matchIfMissing = true)
public class LocalTerminalReservationCompensatorServiceAdapter implements TerminalReservationCompensatorServicePort {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void release(UUID terminalId, UUID terminalRequestId) {
        logger.info("[LOCAL] Releasing terminal reservation. TerminalId: {}, RequestId: {}", terminalId, terminalRequestId);
    }
}
