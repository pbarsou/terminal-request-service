package com.desafio.terminalrequestservice.domain.entity;

public record Address(
        String street,
        String number,
        String city,
        String state,
        String zipCode
) {}
