package com.desafio.terminalrequest.application.listeners.events;

import com.desafio.terminalrequest.domain.events.TerminalReservationReservationConfirmed;
import com.desafio.terminalrequest.fixtures.AddressFixture;
import com.desafio.terminalrequest.usecase.ProcessTerminalRequestReservationConfirmedUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TerminalRequestReservationConfirmedListenerTest {

    @Mock
    private ProcessTerminalRequestReservationConfirmedUseCase useCase;

    @InjectMocks
    private TerminalRequestReservationConfirmedListener listener;

    @Test
    void shouldCallUseCaseWhenEventReceived() {
        var event = new TerminalReservationReservationConfirmed(
                AddressFixture.createAddress(),
                UUID.randomUUID(),
                UUID.randomUUID()
        );

        listener.handleRequest(event);

        verify(useCase).execute(event);
    }
}
