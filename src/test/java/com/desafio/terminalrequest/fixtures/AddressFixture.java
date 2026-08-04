package com.desafio.terminalrequest.fixtures;

import com.desafio.terminalrequest.domain.entity.terminalrequest.Address;

public class AddressFixture {
  public static Address createAddress() {
    return new Address("Rua do Carimbo", "100", "São Paulo", "SP", "12345");
  }
}
