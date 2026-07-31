package com.desafio.terminalrequest.domain.entity.terminalrequest;

import com.desafio.terminalrequest.domain.enums.TerminalRequestsStatus;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "terminal_request")
public class TerminalRequestTable {
    @Id
    private UUID id;
    @Enumerated(EnumType.STRING)
    private TerminalRequestsStatus status;
    private String customerId;
    private String terminalType;
    @Embedded
    private Address address;
    private Instant createdAt;
    private Instant updatedAt;

    public TerminalRequest toDomain() {
        return new TerminalRequest(
                this.status,
                this.customerId,
                this.terminalType,
                this.address,
                this.updatedAt
        );
    }

    public static TerminalRequestTable toModel(TerminalRequest terminalRequest) {
        return new TerminalRequestTable(
                terminalRequest.getId(),
                terminalRequest.getStatus(),
                terminalRequest.getCustomerId(),
                terminalRequest.getTerminalType(),
                terminalRequest.getAddress(),
                terminalRequest.getCreatedAt(),
                terminalRequest.getUpdatedAt()
        );
    }

    protected TerminalRequestTable() {
    }

    public TerminalRequestTable(
            UUID id,
            TerminalRequestsStatus status,
            String customerId,
            String terminalType,
            Address address,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.status = status;
        this.customerId = customerId;
        this.terminalType = terminalType;
        this.address = address;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public TerminalRequestsStatus getStatus() {
        return status;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
