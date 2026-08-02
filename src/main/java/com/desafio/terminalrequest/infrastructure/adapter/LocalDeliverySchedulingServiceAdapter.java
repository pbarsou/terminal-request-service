package com.desafio.terminalrequest.infrastructure.adapter;

import com.desafio.terminalrequest.domain.entity.terminalrequest.Address;
import com.desafio.terminalrequest.domain.service.DeliverySchedulingServicePort;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@ConditionalOnProperty(name = "aws.lambda.enabled", havingValue = "false", matchIfMissing = true)
public class LocalDeliverySchedulingServiceAdapter implements DeliverySchedulingServicePort {

    @Override
    public UUID scheduleDelivery(Address address, UUID terminalRequestId, UUID terminalId) {
        return UUID.randomUUID();
    }
}
