package com.desafio.terminalrequest.application.listeners.events;

import com.desafio.terminalrequest.domain.events.TerminalRequestCreated;
import com.desafio.terminalrequest.usecase.ProcessTerminalRequestCreatedUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class TerminalRequestCreatedListener {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final ProcessTerminalRequestCreatedUseCase processTerminalRequestCreatedUseCase;

  public TerminalRequestCreatedListener(
      ProcessTerminalRequestCreatedUseCase processTerminalRequestCreatedUseCase) {
    this.processTerminalRequestCreatedUseCase = processTerminalRequestCreatedUseCase;
  }

  @Async
  @EventListener
  public void handleRequest(TerminalRequestCreated event) {
    logger.debug("Validating customer {}", event.customerId());
    processTerminalRequestCreatedUseCase.execute(event);
  }
}
