package com.desafio.terminalrequest.domain.service;

import com.desafio.terminalrequest.domain.entity.terminalrequest.Address;
import java.util.UUID;

public interface DeliverySchedulingServicePort {
  UUID scheduleDelivery(Address address, UUID terminalRequestId, UUID terminalId);
}
