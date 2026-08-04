package com.desafio.terminalrequest.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.desafio.terminalrequest.domain.entity.terminalrequest.TerminalRequest;
import com.desafio.terminalrequest.domain.enums.TerminalRequestsStatus;
import com.desafio.terminalrequest.domain.events.TerminalRequestCreated;
import com.desafio.terminalrequest.domain.events.TerminalRequestCustomerValidated;
import com.desafio.terminalrequest.domain.service.CustomerValidationServicePort;
import com.desafio.terminalrequest.domain.service.TerminalRequestServicePort;
import com.desafio.terminalrequest.fixtures.TerminalRequestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
public class ProcessTerminalRequestCreatedUseCaseTest {

  @Mock private TerminalRequestServicePort terminalRequestService;

  @Mock private CustomerValidationServicePort customerService;

  private ProcessTerminalRequestCreatedUseCase useCase;

  @Mock private ApplicationEventPublisher publisher;

  @BeforeEach
  void setUp() {
    useCase =
        new ProcessTerminalRequestCreatedUseCase(
            terminalRequestService, customerService, publisher);
  }

  @Test
  @DisplayName("Should validate customer and update status to VALIDADO when customer is active")
  void shouldValidateCustomerAndUpdateStatusToValidadoWhenCustomerIsActive() {
    TerminalRequest terminalRequest = TerminalRequestFixture.createTerminalRequest();
    TerminalRequestCreated event =
        new TerminalRequestCreated(
            terminalRequest.getCustomerId(),
            terminalRequest.getId(),
            terminalRequest.getTerminalType(),
            terminalRequest.getAddress());

    when(customerService.isActiveCustomer(event.customerId())).thenReturn(true);

    useCase.execute(event);

    verify(customerService).isActiveCustomer(event.customerId());
    verify(terminalRequestService)
        .updateStatus(event.terminalRequestId(), TerminalRequestsStatus.VALIDADO);

    ArgumentCaptor<TerminalRequestCustomerValidated> eventCaptor =
        ArgumentCaptor.forClass(TerminalRequestCustomerValidated.class);
    verify(publisher).publishEvent(eventCaptor.capture());

    TerminalRequestCustomerValidated publishedEvent = eventCaptor.getValue();
    assertThat(publishedEvent.terminalRequestId()).isEqualTo(event.terminalRequestId());
    assertThat(publishedEvent.terminalType()).isEqualTo(event.terminalType());
    assertThat(publishedEvent.address()).isEqualTo(event.address());
  }

  @Test
  @DisplayName("Should update status to REJEITADO when customer is inactive")
  void shouldUpdateStatusToRejeitadoWhenCustomerIsInactive() {
    var terminalRequest = TerminalRequestFixture.createTerminalRequest();
    var event =
        new TerminalRequestCreated(
            terminalRequest.getCustomerId(),
            terminalRequest.getId(),
            terminalRequest.getTerminalType(),
            terminalRequest.getAddress());

    when(customerService.isActiveCustomer(event.customerId())).thenReturn(false);

    useCase.execute(event);

    verify(customerService).isActiveCustomer(event.customerId());
    verify(terminalRequestService)
        .updateStatus(event.terminalRequestId(), TerminalRequestsStatus.REJEITADO);
    verify(publisher, never()).publishEvent(any());
  }
}
