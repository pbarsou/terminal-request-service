package com.desafio.terminalrequest.domain.entity.terminalrequest;

import com.desafio.terminalrequest.domain.enums.TerminalRequestsStatus;

import java.util.Objects;
import java.util.UUID;

public class TerminalRequest {
    private final UUID id;
    private TerminalRequestsStatus status;
    private final String customerId;
    private final String terminalType;
    private final Address address;

    public TerminalRequest(UUID id, String customerId, String terminalType, Address address) {
        this.id = id;
        this.customerId = customerId;
        this.terminalType = terminalType;
        this.address = address;
        this.status = TerminalRequestsStatus.SOLICITADO;
    }

    public TerminalRequest(UUID id, TerminalRequestsStatus status, String customerId, String terminalType, Address address) {
        this.id = id;
        this.customerId = customerId;
        this.terminalType = terminalType;
        this.address = address;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public TerminalRequestsStatus getStatus() {
        return status;
    }

    public void setStatus(TerminalRequestsStatus status) {
        this.status = status;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public Address getAddress() {
        return address;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TerminalRequest that = (TerminalRequest) o;
        return Objects.equals(id, that.id) && Objects.equals(customerId, that.customerId) && Objects.equals(terminalType, that.terminalType) && Objects.equals(address, that.address) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerId, terminalType, address, status);
    }
}
