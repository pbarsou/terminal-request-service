package com.desafio.terminalrequest.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.desafio.terminalrequest.application.api.command.CreateTerminalRequestCommand;
import com.desafio.terminalrequest.domain.entity.terminalrequest.TerminalRequest;
import com.desafio.terminalrequest.domain.events.TerminalRequestCreated;
import com.desafio.terminalrequest.domain.service.TerminalRequestServicePort;
import com.desafio.terminalrequest.fixtures.TerminalRequestCommandFixture;
import com.desafio.terminalrequest.infrastructure.config.datadog.event.StatsService;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class CreateTerminalRequestUseCaseTest {

  @Mock private TerminalRequestServicePort terminalRequestService;

  @Mock private ApplicationEventPublisher publisher;

  @Mock private StatsService statsService;

  private CreateTerminalRequestUseCase useCase;

  @BeforeEach
  void setUp() {
    useCase = new CreateTerminalRequestUseCase(terminalRequestService, publisher, statsService);
  }

  @Test
  @DisplayName("Should create terminal request and publish TerminalRequestCreated event")
  void shouldCreateTerminalRequest() {
    CreateTerminalRequestCommand command = TerminalRequestCommandFixture.createCommand();
    TerminalRequest savedRequest =
        new TerminalRequest(command.customerId(), command.terminalType(), command.address());
    when(terminalRequestService.insertTerminalRequest(any(TerminalRequest.class)))
        .thenReturn(savedRequest);

    UUID resultId = useCase.execute(command);

    assertThat(resultId).isEqualTo(savedRequest.getId());
    verify(terminalRequestService).insertTerminalRequest(any(TerminalRequest.class));
    verify(statsService).recordEvent(eq(StatsService.CustomEvents.TERMINAL_REQUEST_CREATED), any());

    ArgumentCaptor<TerminalRequestCreated> eventCaptor =
        ArgumentCaptor.forClass(TerminalRequestCreated.class);
    verify(publisher).publishEvent(eventCaptor.capture());

    TerminalRequestCreated publishedEvent = eventCaptor.getValue();
    assertThat(publishedEvent.customerId()).isEqualTo(command.customerId());
    assertThat(publishedEvent.terminalRequestId()).isEqualTo(savedRequest.getId());
  }
}
