package com.desafio.terminalrequest.fixtures;

import com.desafio.terminalrequest.domain.entity.terminalrequest.TerminalRequest;
import com.desafio.terminalrequest.domain.enums.TerminalType;

public class TerminalRequestFixture {

    public static TerminalRequest createTerminalRequest() {
        return new TerminalRequest("CUST-1", TerminalType.POS_WIFI, AddressFixture.createAddress());
    }
}
