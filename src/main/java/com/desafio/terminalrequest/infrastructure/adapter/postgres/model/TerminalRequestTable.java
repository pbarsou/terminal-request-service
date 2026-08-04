package com.desafio.terminalrequest.infrastructure.adapter.postgres.model;

import com.desafio.terminalrequest.domain.entity.terminalrequest.Address;
import com.desafio.terminalrequest.domain.entity.terminalrequest.TerminalRequest;
import com.desafio.terminalrequest.domain.enums.TerminalRequestsStatus;
import com.desafio.terminalrequest.domain.enums.TerminalType;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "terminal_request")
public class TerminalRequestTable {
  @Id private UUID id;

  @Enumerated(EnumType.STRING)
  private TerminalRequestsStatus status;

  private String customerId;

  @Enumerated(EnumType.STRING)
  private TerminalType terminalType;

  private UUID terminalId;
  private UUID trackingId;
  @Embedded private Address address;
  private Instant createdAt;
  private Instant updatedAt;

  public TerminalRequest toDomain() {
    return new TerminalRequest(
        Objects.requireNonNull(this.id, "Terminal Request ID must not be null"),
        this.status,
        this.customerId,
        this.terminalType,
        this.terminalId,
        this.trackingId,
        this.address,
        this.createdAt,
        this.updatedAt);
  }

  public static TerminalRequestTable toModel(TerminalRequest terminalRequest) {
    return new TerminalRequestTable(
        terminalRequest.getId(),
        terminalRequest.getStatus(),
        terminalRequest.getCustomerId(),
        terminalRequest.getTerminalType(),
        terminalRequest.getTerminalId(),
        terminalRequest.getTrackingId(),
        terminalRequest.getAddress(),
        terminalRequest.getCreatedAt(),
        terminalRequest.getUpdatedAt());
  }

  protected TerminalRequestTable() {}

  public TerminalRequestTable(
      UUID id,
      TerminalRequestsStatus status,
      String customerId,
      TerminalType terminalType,
      UUID terminalId,
      UUID trackingId,
      Address address,
      Instant createdAt,
      Instant updatedAt) {
    this.id = id;
    this.status = status;
    this.customerId = customerId;
    this.terminalType = terminalType;
    this.terminalId = terminalId;
    this.trackingId = trackingId;
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

  public TerminalType getTerminalType() {
    return terminalType;
  }

  public Address getAddress() {
    return address;
  }

  public UUID getTerminalId() {
    return terminalId;
  }

  public UUID getTrackingId() {
    return trackingId;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }
}
