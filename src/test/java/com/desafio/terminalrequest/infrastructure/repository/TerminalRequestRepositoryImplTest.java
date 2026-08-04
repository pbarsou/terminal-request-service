package com.desafio.terminalrequest.infrastructure.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.desafio.terminalrequest.domain.entity.terminalrequest.TerminalRequest;
import com.desafio.terminalrequest.domain.entity.terminalrequest.TerminalRequestTable;
import com.desafio.terminalrequest.fixtures.TerminalRequestFixture;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TerminalRequestRepositoryImplTest {

  @Mock private TerminalRequestPostgresRepository postgresRepository;

  private TerminalRequestRepositoryImpl repository;

  @BeforeEach
  void setUp() {
    repository = new TerminalRequestRepositoryImpl(postgresRepository);
  }

  @Test
  @DisplayName("Should save terminal request successfully")
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
  @DisplayName("Should return terminal request when ID exists in database")
  void shouldGetById() {
    TerminalRequest terminalRequest = TerminalRequestFixture.createTerminalRequest();
    TerminalRequestTable terminalRequestTable = TerminalRequestTable.toModel(terminalRequest);
    when(postgresRepository.findById(terminalRequest.getId()))
        .thenReturn(Optional.of(terminalRequestTable));

    Optional<TerminalRequest> result = repository.getById(terminalRequest.getId());

    assertThat(result).isPresent();
    assertThat(result.get().getId()).isEqualTo(terminalRequest.getId());
    verify(postgresRepository).findById(terminalRequest.getId());
  }
}
