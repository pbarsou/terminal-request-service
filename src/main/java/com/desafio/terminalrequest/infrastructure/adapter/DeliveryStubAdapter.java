package com.desafio.terminalrequest.infrastructure.adapter;

import com.desafio.terminalrequest.domain.entity.terminalrequest.Address;
import com.desafio.terminalrequest.domain.service.DeliveryServicePort;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DeliveryStubAdapter implements DeliveryServicePort {

    @Override
    public UUID scheduleDelivery(Address address, UUID terminalRequestId, UUID terminalId) {
        // Retorna um ID aleatório para simular o agendamento de entrega
        return UUID.randomUUID();
    }
}
