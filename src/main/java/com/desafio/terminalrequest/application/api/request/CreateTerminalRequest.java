package com.desafio.terminalrequest.application.api.request;

import com.desafio.terminalrequest.domain.entity.terminalrequest.Address;

public record CreateTerminalRequest(
    String customerId,
    String terminalType,
    Address address
) {}
