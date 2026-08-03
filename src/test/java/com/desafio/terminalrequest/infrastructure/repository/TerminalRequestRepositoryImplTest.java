package com.desafio.terminalrequest.infrastructure.repository;

import com.desafio.terminalrequest.domain.entity.terminalrequest.TerminalRequest;
import com.desafio.terminalrequest.domain.entity.terminalrequest.TerminalRequestTable;
import com.desafio.terminalrequest.fixtures.TerminalRequestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TerminalRequestRepositoryImplTest {

    @Mock
    private TerminalRequestPostgresRepository postgresRepository;

    private TerminalRequestRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        repository = new TerminalRequestRepositoryImpl(postgresRepository);
    }

    @Test
    void shouldSaveTerminalRequest() {
        TerminalRequest terminalRequest = TerminalRequestFixture.createTerminalRequest();
        TerminalRequestTable terminalRequestTable = TerminalRequestTable.toModel(terminalRequest);
        when(postgresRepository.save(any(TerminalRequestTable.class))).thenReturn(terminalRequestTable);

        TerminalRequest result = repository.save(terminalRequest);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(terminalRequest.getId());
        verify(postgresRepository).save(any(TerminalRequestTable.class));
    }

    @Test
    void shouldGetById() {
        TerminalRequest terminalRequest = TerminalRequestFixture.createTerminalRequest();
        TerminalRequestTable terminalRequestTable = TerminalRequestTable.toModel(terminalRequest);
        when(postgresRepository.findById(terminalRequest.getId())).thenReturn(Optional.of(terminalRequestTable));

        Optional<TerminalRequest> result = repository.getById(terminalRequest.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(terminalRequest.getId());
        verify(postgresRepository).findById(terminalRequest.getId());
    }
}
