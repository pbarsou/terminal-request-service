package com.desafio.terminalrequest.application.api.command;

import com.desafio.terminalrequest.domain.entity.terminalrequest.Address;
import com.desafio.terminalrequest.domain.enums.TerminalType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTerminalRequestCommand(
    @NotBlank String customerId, @NotNull TerminalType terminalType, @Valid Address address) {}
