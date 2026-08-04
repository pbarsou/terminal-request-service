package com.desafio.terminalrequest.infrastructure.repository;

import com.desafio.terminalrequest.domain.entity.terminalrequest.TerminalRequest;
import com.desafio.terminalrequest.domain.repository.TerminalRequestRepository;
import com.desafio.terminalrequest.infrastructure.adapter.postgres.model.TerminalRequestTable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
public class TerminalRequestRepositoryImpl implements TerminalRequestRepository {

  private final TerminalRequestPostgresRepository postgresRepository;

  TerminalRequestRepositoryImpl(TerminalRequestPostgresRepository postgresRepository) {
    this.postgresRepository = postgresRepository;
  }

  @Override
  public TerminalRequest save(TerminalRequest terminalRequest) {
    return postgresRepository.save(TerminalRequestTable.toModel(terminalRequest)).toDomain();
  }

  @Override
  public Optional<TerminalRequest> getById(UUID id) {
    return postgresRepository.findById(id).map(TerminalRequestTable::toDomain);
  }
}

@Repository
interface TerminalRequestPostgresRepository extends JpaRepository<TerminalRequestTable, UUID> {
  List<TerminalRequestTable> findAllByOrderByUpdatedAtDesc();
}
