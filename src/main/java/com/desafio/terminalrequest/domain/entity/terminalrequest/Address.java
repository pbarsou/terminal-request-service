package com.desafio.terminalrequest.domain.entity.terminalrequest;

import jakarta.persistence.Embeddable;

@Embeddable
public record Address(String street, String number, String city, String state, String zipCode) {}
