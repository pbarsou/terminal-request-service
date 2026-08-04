package com.desafio.terminalrequest.application.listeners.events;

import static org.mockito.Mockito.verify;

import com.desafio.terminalrequest.domain.events.TerminalRequestCreated;
import com.desafio.terminalrequest.fixtures.TerminalRequestFixture;
import com.desafio.terminalrequest.usecase.ProcessTerminalRequestCreatedUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TerminalRequestCreatedListenerTest {

  @Mock private ProcessTerminalRequestCreatedUseCase useCase;

  @InjectMocks private TerminalRequestCreatedListener listener;

  @Test
  @DisplayName("Should call use case when event is received")
  void shouldCallUseCaseWhenEventReceived() {
    var terminalRequest = TerminalRequestFixture.createTerminalRequest();
    var event =
        new TerminalRequestCreated(
            terminalRequest.getCustomerId(),
            terminalRequest.getId(),
            terminalRequest.getTerminalType(),
            terminalRequest.getAddress());

    listener.handleRequest(event);

    verify(useCase).execute(event);
  }
}
