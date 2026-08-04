package com.desafio.terminalrequest.usecase;

import com.desafio.terminalrequest.domain.events.TerminalRequestDeliverySchedulingFailed;
import com.desafio.terminalrequest.domain.service.TerminalReservationCompensatorServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProcessTerminalRequestDeliverySchedulingFailedUseCaseTest {

    @Mock
    private TerminalReservationCompensatorServicePort compensatorService;

    private ProcessTerminalRequestDeliverySchedulingFailedUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ProcessTerminalRequestDeliverySchedulingFailedUseCase(compensatorService);
    }

    @Test
    @DisplayName("Should compensate terminal reservation when delivery fails")
    void shouldCompensateTerminalReservationWhenDeliveryFails() {
        UUID terminalId = UUID.randomUUID();
        UUID requestId = UUID.randomUUID();

        var event = new TerminalRequestDeliverySchedulingFailed(
                terminalId,
                requestId
        );

        useCase.execute(event);

        verify(compensatorService).release(terminalId, requestId);
    }
}
