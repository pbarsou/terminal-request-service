package com.desafio.terminalrequest.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.desafio.terminalrequest.domain.entity.terminalrequest.TerminalRequest;
import com.desafio.terminalrequest.domain.enums.TerminalRequestsStatus;
import com.desafio.terminalrequest.domain.events.TerminalRequestDeliverySchedulingFailed;
import com.desafio.terminalrequest.domain.events.TerminalReservationReservationConfirmed;
import com.desafio.terminalrequest.domain.service.DeliverySchedulingServicePort;
import com.desafio.terminalrequest.domain.service.TerminalRequestServicePort;
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
class ProcessTerminalRequestReservationConfirmedUseCaseTest {

  @Mock private TerminalRequestServicePort terminalRequestService;

  @Mock private DeliverySchedulingServicePort deliveryService;

  @Mock private ApplicationEventPublisher publisher;

  private ProcessTerminalRequestReservationConfirmedUseCase useCase;

  @BeforeEach
  void setUp() {
    useCase =
        new ProcessTerminalRequestReservationConfirmedUseCase(
            terminalRequestService, deliveryService, publisher);
  }

  @Test
  @DisplayName(
      "Should schedule delivery and update status to AGENDADO when reservation is confirmed")
  void shouldScheduleDeliveryAndUpdateStatusToAgendadoWhenReservationIsConfirmed() {
    TerminalRequest terminalRequest = TerminalRequestFixture.createTerminalRequest();
    UUID terminalId = UUID.randomUUID();
    var event =
        new TerminalReservationReservationConfirmed(
            terminalRequest.getAddress(), terminalId, terminalRequest.getId());
    UUID trackingId = UUID.randomUUID();

    when(deliveryService.scheduleDelivery(
            event.address(), event.terminalRequestId(), event.terminalId()))
        .thenReturn(trackingId);

    useCase.execute(event);

    verify(deliveryService)
        .scheduleDelivery(event.address(), event.terminalRequestId(), event.terminalId());
    verify(terminalRequestService)
        .updateStatus(event.terminalRequestId(), TerminalRequestsStatus.AGENDADO);
    verify(terminalRequestService).assignTracking(event.terminalRequestId(), trackingId);
    verifyNoInteractions(publisher);
  }

  @Test
  @DisplayName(
      "Should update status to ERRO_AGENDAMENTO and publish failed event when delivery scheduling fails")
  void shouldUpdateStatusToErroAgendamentoAndPublishFailedEventWhenDeliverySchedulingFails() {
    TerminalRequest terminalRequest = TerminalRequestFixture.createTerminalRequest();
    var event =
        new TerminalReservationReservationConfirmed(
            terminalRequest.getAddress(), UUID.randomUUID(), UUID.randomUUID());

    when(deliveryService.scheduleDelivery(any(), any(), any())).thenReturn(null);

    useCase.execute(event);

    verify(terminalRequestService)
        .updateStatus(event.terminalRequestId(), TerminalRequestsStatus.ERRO_AGENDAMENTO);
    verify(terminalRequestService, never()).assignTracking(any(), any());

    ArgumentCaptor<TerminalRequestDeliverySchedulingFailed> eventCaptor =
        ArgumentCaptor.forClass(TerminalRequestDeliverySchedulingFailed.class);
    verify(publisher).publishEvent(eventCaptor.capture());

    TerminalRequestDeliverySchedulingFailed publishedEvent = eventCaptor.getValue();
    assertThat(publishedEvent.terminalId()).isEqualTo(event.terminalId());
    assertThat(publishedEvent.terminalRequestId()).isEqualTo(event.terminalRequestId());
  }
}
