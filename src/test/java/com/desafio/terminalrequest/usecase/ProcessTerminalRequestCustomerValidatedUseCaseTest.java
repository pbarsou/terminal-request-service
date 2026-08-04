package com.desafio.terminalrequest.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.desafio.terminalrequest.domain.enums.TerminalRequestsStatus;
import com.desafio.terminalrequest.domain.events.TerminalRequestCustomerValidated;
import com.desafio.terminalrequest.domain.events.TerminalReservationReservationConfirmed;
import com.desafio.terminalrequest.domain.service.TerminalRequestServicePort;
import com.desafio.terminalrequest.domain.service.TerminalReservationServicePort;
import com.desafio.terminalrequest.fixtures.TerminalRequestFixture;
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
public class ProcessTerminalRequestCustomerValidatedUseCaseTest {

  @Mock private TerminalRequestServicePort terminalRequestService;

  @Mock private TerminalReservationServicePort terminalReservationService;

  @Mock private ApplicationEventPublisher publisher;

  private ProcessTerminalRequestCustomerValidatedUseCase useCase;

  @BeforeEach
  void setUp() {
    useCase =
        new ProcessTerminalRequestCustomerValidatedUseCase(
            terminalRequestService, terminalReservationService, publisher);
  }

  @Test
  @DisplayName("Should reserve a terminal and update status to RESERVADO when reserve confirmed")
  void shouldReserveATerminalAndUpdateStatusToReservadoWhenReserveConfirmed() {
    var terminalRequest = TerminalRequestFixture.createTerminalRequest();
    var event =
        new TerminalRequestCustomerValidated(
            terminalRequest.getTerminalType(),
            terminalRequest.getId(),
            terminalRequest.getAddress());
    UUID terminalId = UUID.randomUUID();

    when(terminalReservationService.reserveATerminal(
            event.terminalType(), event.terminalRequestId()))
        .thenReturn(terminalId);

    useCase.execute(event);

    verify(terminalReservationService)
        .reserveATerminal(event.terminalType(), event.terminalRequestId());
    verify(terminalRequestService)
        .updateStatus(event.terminalRequestId(), TerminalRequestsStatus.RESERVADO);
    verify(terminalRequestService).assignTerminal(event.terminalRequestId(), terminalId);

    ArgumentCaptor<TerminalReservationReservationConfirmed> eventCaptor =
        ArgumentCaptor.forClass(TerminalReservationReservationConfirmed.class);
    verify(publisher).publishEvent(eventCaptor.capture());

    var publishedEvent = eventCaptor.getValue();
    assertThat(publishedEvent.terminalId()).isEqualTo(terminalId);
    assertThat(publishedEvent.terminalRequestId()).isEqualTo(event.terminalRequestId());
  }

  @Test
  @DisplayName("Should update status to ERRO_RESERVA if reserve a terminal failed")
  void shouldUpdateStatusToErroReservaIfReserveATerminalFailed() {
    var terminalRequest = TerminalRequestFixture.createTerminalRequest();
    var event =
        new TerminalRequestCustomerValidated(
            terminalRequest.getTerminalType(),
            terminalRequest.getId(),
            terminalRequest.getAddress());

    when(terminalReservationService.reserveATerminal(any(), any())).thenReturn(null);

    useCase.execute(event);

    verify(terminalRequestService)
        .updateStatus(event.terminalRequestId(), TerminalRequestsStatus.ERRO_RESERVA);
    verify(publisher, never()).publishEvent(any());
    verify(terminalRequestService, never()).assignTerminal(any(), any());
  }
}
