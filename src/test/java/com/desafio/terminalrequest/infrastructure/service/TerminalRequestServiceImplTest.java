package com.desafio.terminalrequest.infrastructure.service;

import com.desafio.terminalrequest.domain.entity.terminalrequest.TerminalRequest;
import com.desafio.terminalrequest.domain.enums.TerminalRequestsStatus;
import com.desafio.terminalrequest.domain.exceptions.TerminalRequestNotFoundException;
import com.desafio.terminalrequest.domain.repository.TerminalRequestRepository;
import com.desafio.terminalrequest.fixtures.TerminalRequestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TerminalRequestServiceImplTest {

    @Mock
    private TerminalRequestRepository repository;

    private TerminalRequestServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new TerminalRequestServiceImpl(repository);
    }

    @Nested
    @DisplayName("Insert Terminal Request")
    class InsertTerminalRequest {
        @Test
        @DisplayName("Should insert terminal request successfully")
        void shouldInsertTerminalRequest() {
            TerminalRequest terminalRequest = TerminalRequestFixture.createTerminalRequest();
            when(repository.save(any(TerminalRequest.class))).thenReturn(terminalRequest);

            TerminalRequest result = service.insertTerminalRequest(terminalRequest);

            assertThat(result).isNotNull();
            verify(repository).save(terminalRequest);
        }
    }

    @Nested
    @DisplayName("Get Terminal Request By Id")
    class GetTerminalRequestById {
        @Test
        @DisplayName("Should return terminal request when ID exists in database")
        void shouldGetTerminalRequestById() {
            TerminalRequest terminalRequest = TerminalRequestFixture.createTerminalRequest();
            when(repository.getById(terminalRequest.getId())).thenReturn(Optional.of(terminalRequest));

            Optional<TerminalRequest> result = service.getTerminalRequestById(terminalRequest.getId());

            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(terminalRequest);
            verify(repository).getById(terminalRequest.getId());
        }
    }

    @Nested
    @DisplayName("Update Status")
    class UpdateStatus {
        @Test
        @DisplayName("Should update status successfully when ID exists")
        void shouldUpdateStatus() {
            TerminalRequest terminalRequest = TerminalRequestFixture.createTerminalRequest();
            when(repository.getById(terminalRequest.getId())).thenReturn(Optional.of(terminalRequest));

            service.updateStatus(terminalRequest.getId(), TerminalRequestsStatus.VALIDADO);

            assertThat(terminalRequest.getStatus()).isEqualTo(TerminalRequestsStatus.VALIDADO);
            verify(repository).save(terminalRequest);
        }

        @Test
        @DisplayName("Should throw exception when updating status for non-existent terminal request")
        void shouldThrowExceptionWhenTryUpdatingStatusForNonExistentTerminalRequest() {
            UUID id = UUID.randomUUID();
            when(repository.getById(id)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.updateStatus(id, TerminalRequestsStatus.VALIDADO))
                    .isInstanceOf(TerminalRequestNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Assign Terminal")
    class AssignTerminal {
        @Test
        @DisplayName("Should assign terminal ID successfully when terminal request exists")
        void shouldAssignTerminal() {
            UUID terminalId = UUID.randomUUID();
            TerminalRequest terminalRequest = TerminalRequestFixture.createTerminalRequest();
            when(repository.getById(terminalRequest.getId())).thenReturn(Optional.of(terminalRequest));

            service.assignTerminal(terminalRequest.getId(), terminalId);

            assertThat(terminalRequest.getTerminalId()).isEqualTo(terminalId);
            verify(repository).save(terminalRequest);
        }

        @Test
        @DisplayName("Should throw exception when assigning terminal ID for non-existent terminal request")
        void shouldThrowExceptionWhenAssigningTerminalForNonExistentTerminalRequest() {
            UUID id = UUID.randomUUID();
            when(repository.getById(id)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.assignTerminal(id, UUID.randomUUID()))
                    .isInstanceOf(TerminalRequestNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Assign Tracking")
    class AssignTracking {
        @Test
        @DisplayName("Should assign tracking ID successfully when terminal request exists")
        void shouldAssignTracking() {
            UUID trackingId = UUID.randomUUID();
            TerminalRequest terminalRequest = TerminalRequestFixture.createTerminalRequest();
            when(repository.getById(terminalRequest.getId())).thenReturn(Optional.of(terminalRequest));

            service.assignTracking(terminalRequest.getId(), trackingId);

            assertThat(terminalRequest.getTrackingId()).isEqualTo(trackingId);
            verify(repository).save(terminalRequest);
        }

        @Test
        @DisplayName("Should throw exception when assigning tracking ID for non-existent terminal request")
        void shouldThrowExceptionWhenAssigningTrackingForNonExistentTerminalRequest() {
            UUID id = UUID.randomUUID();
            when(repository.getById(id)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.assignTracking(id, UUID.randomUUID()))
                    .isInstanceOf(TerminalRequestNotFoundException.class);
        }
    }
}
