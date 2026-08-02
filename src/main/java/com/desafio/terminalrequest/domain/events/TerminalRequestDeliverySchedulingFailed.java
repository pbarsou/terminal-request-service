package com.desafio.terminalrequest.domain.events;

import java.util.UUID;

public record TerminalRequestDeliverySchedulingFailed(UUID terminalId, UUID terminalRequestId) { }
