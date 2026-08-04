package com.desafio.terminalrequest.domain.service;

public interface CustomerValidationServicePort {
  boolean isActiveCustomer(String customerId);
}
