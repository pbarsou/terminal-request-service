package com.desafio.terminalrequest.application.listeners.events;

import static org.mockito.Mockito.verify;

import com.desafio.terminalrequest.domain.events.TerminalRequestCustomerValidated;
import com.desafio.terminalrequest.fixtures.TerminalRequestFixture;
import com.desafio.terminalrequest.usecase.ProcessTerminalRequestCustomerValidatedUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TerminalRequestCustomerValidatedListenerTest {

  @Mock private ProcessTerminalRequestCustomerValidatedUseCase useCase;

  @InjectMocks private TerminalRequestCustomerValidatedListener listener;

  @Test
  @DisplayName("Should call use case when event is received")
  void shouldCallUseCaseWhenEventReceived() {
    var terminalRequest = TerminalRequestFixture.createTerminalRequest();
    var event =
        new TerminalRequestCustomerValidated(
            terminalRequest.getTerminalType(),
            terminalRequest.getId(),
            terminalRequest.getAddress());

    listener.handleRequest(event);

    verify(useCase).execute(event);
  }
}
