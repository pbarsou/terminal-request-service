package com.desafio.terminalrequest.infrastructure.repository;

import com.desafio.terminalrequest.domain.entity.terminalrequest.TerminalRequest;
import com.desafio.terminalrequest.domain.entity.terminalrequest.TerminalRequestTable;
import com.desafio.terminalrequest.domain.repository.TerminalRequestRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Component
public class TerminalRequestRepositoryImpl implements TerminalRequestRepository {

    private final TerminalRequestPostgresRepository postgresRepository;

    public TerminalRequestRepositoryImpl(TerminalRequestPostgresRepository postgresRepository) {
        this.postgresRepository = postgresRepository;
    }

    @Override
    public TerminalRequest save(TerminalRequest terminalRequest) {
        return postgresRepository.save(TerminalRequestTable.toModel(terminalRequest)).toDomain();
    }

    @Override
    public Optional<TerminalRequest> getById(UUID id) {
        return postgresRepository.getReferenceById(id).toDomain() != null ?
                Optional.of(postgresRepository.getReferenceById(id).toDomain()) : Optional.empty();
    }
}

@Repository
interface TerminalRequestPostgresRepository extends JpaRepository<TerminalRequestTable, UUID> {
}
