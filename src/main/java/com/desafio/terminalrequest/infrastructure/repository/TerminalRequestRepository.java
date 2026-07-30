package com.desafio.terminalrequest.infrastructure.repository;

import com.desafio.terminalrequest.domain.entity.terminalrequest.TerminalRequestTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TerminalRequestRepository extends JpaRepository<TerminalRequestTable, UUID> {
}
