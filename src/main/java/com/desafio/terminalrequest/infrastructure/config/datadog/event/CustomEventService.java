package com.desafio.terminalrequest.infrastructure.config.datadog.event;

import java.util.Map;

public abstract class CustomEventService {

  public abstract void recordEvent(CustomEvents event, Map<String, String> attributes);

  public enum CustomEvents {
    TERMINAL_REQUEST_CREATED("TerminalRequestCreated"),
    CUSTOMER_VALIDATION_SUCCESS("CustomerValidationSuccess"),
    CUSTOMER_VALIDATION_FAILED("CustomerValidationFailed"),
    TERMINAL_RESERVATION_SUCCESS("TerminalReservationSuccess"),
    TERMINAL_RESERVATION_FAILED("TerminalReservationFailed"),
    DELIVERY_SCHEDULING_SUCCESS("DeliverySchedulingSuccess"),
    DELIVERY_SCHEDULING_FAILED("DeliverySchedulingFailed"),
    TERMINAL_RESERVATION_COMPENSATED("TerminalReservationCompensated");

    private final String eventName;

    CustomEvents(String eventName) {
      this.eventName = eventName;
    }

    public String getEventName() {
      return eventName;
    }
  }
}
