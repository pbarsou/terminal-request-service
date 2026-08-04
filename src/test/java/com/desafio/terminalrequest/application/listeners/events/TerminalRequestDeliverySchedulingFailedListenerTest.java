package com.desafio.terminalrequest.application.listeners.events;

import com.desafio.terminalrequest.domain.events.TerminalRequestDeliverySchedulingFailed;
import com.desafio.terminalrequest.usecase.ProcessTerminalRequestDeliverySchedulingFailedUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TerminalRequestDeliverySchedulingFailedListenerTest {

    @Mock
    private ProcessTerminalRequestDeliverySchedulingFailedUseCase useCase;

    @InjectMocks
    private TerminalRequestDeliverySchedulingFailedListener listener;

    @Test
    @DisplayName("Should call use case when event is received")
    void shouldCallUseCaseWhenEventReceived() {
        var event = new TerminalRequestDeliverySchedulingFailed(
                UUID.randomUUID(),
                UUID.randomUUID()
        );

        listener.handleRequest(event);

        verify(useCase).execute(event);
    }
}
