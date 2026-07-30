package com.desafio.terminalrequest.domain.entity.terminalrequest;

import com.desafio.terminalrequest.domain.enums.TerminalRequestsStatus;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import java.util.UUID;

@Entity
@Table(name = "terminal_request")
public class TerminalRequestTable {
    @Id
    private UUID id;
    private TerminalRequestsStatus status;
    private String customerId;
    private String terminalType;
    @Embedded
    private Address address;

    public TerminalRequest toDomain() {
        return new TerminalRequest(
                this.id,
                this.status,
                this.customerId,
                this.terminalType,
                this.address
        );
    }

    public static TerminalRequestTable toModel(TerminalRequest terminalRequest) {
        return new TerminalRequestTable(
                terminalRequest.getId(),
                terminalRequest.getStatus(),
                terminalRequest.getCustomerId(),
                terminalRequest.getTerminalType(),
                terminalRequest.getAddress()
        );
    }

    protected TerminalRequestTable() {
    }

    public TerminalRequestTable(
            UUID id,
            TerminalRequestsStatus status,
            String customerId,
            String terminalType,
            Address address
    ) {
        this.id = id;
        this.status = status;
        this.customerId = customerId;
        this.terminalType = terminalType;
        this.address = address;
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
}
