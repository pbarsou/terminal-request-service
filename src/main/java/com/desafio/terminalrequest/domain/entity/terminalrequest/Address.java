package com.desafio.terminalrequest.domain.entity.terminalrequest;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Embeddable
public record Address(
    String street,
    String number,
    String city,
    @Size(min = 2, max = 2) String state,
    @Pattern(regexp = "\\d{5}-?\\d{3}") String zipCode) {}
