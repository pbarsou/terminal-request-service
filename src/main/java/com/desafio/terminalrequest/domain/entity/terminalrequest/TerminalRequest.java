package com.desafio.terminalrequest.domain.entity.terminalrequest;

import com.desafio.terminalrequest.domain.enums.TerminalRequestsStatus;
import com.desafio.terminalrequest.domain.enums.TerminalType;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class TerminalRequest {
    private UUID id = UUID.randomUUID();
    private TerminalRequestsStatus status;
    private final String customerId;
    private final TerminalType terminalType;
    private final Address address;
    private UUID terminalId = null;
    private UUID trackingId = null;
    private final Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

    public TerminalRequest(String customerId, TerminalType terminalType, Address address) {
        this.customerId = customerId;
        this.terminalType = terminalType;
        this.address = address;
        this.status = TerminalRequestsStatus.SOLICITADO;
    }

    public TerminalRequest(
            UUID id,
            TerminalRequestsStatus status,
            String customerId,
            TerminalType terminalType,
            UUID terminalId,
            UUID trackingId,
            Address address,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.status = status;
        this.customerId = customerId;
        this.terminalType = terminalType;
        this.terminalId = terminalId;
        this.trackingId = trackingId;
        this.address = address;
        this.updatedAt = updatedAt;
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

    public TerminalType getTerminalType() {
        return terminalType;
    }

    public Address getAddress() {
        return address;
    }

    public UUID getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(UUID terminalId) {
        this.terminalId = terminalId;
    }

    public UUID getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(UUID trackingId) {
        this.trackingId = trackingId;
    }

    public Instant getCreatedAt() { return createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TerminalRequest that = (TerminalRequest) o;
        return Objects.equals(id, that.id) && status == that.status && Objects.equals(customerId, that.customerId)
                && terminalType == that.terminalType && Objects.equals(address, that.address)
                && Objects.equals(terminalId, that.terminalId) && Objects.equals(trackingId, that.trackingId)
                && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, customerId, terminalType, address, terminalId, trackingId, createdAt, updatedAt);
    }
}
