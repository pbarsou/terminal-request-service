package com.desafio.terminalrequest.infrastructure.config.datadog.event;

import com.desafio.terminalrequest.infrastructure.config.datadog.event.CustomEventService.CustomEvents;
import com.timgroup.statsd.Event;
import com.timgroup.statsd.StatsDClient;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class StatsService extends CustomEventService {

  private final StatsDClient statsDClient;

  public StatsService(StatsDClient statsDClient) {
    this.statsDClient = statsDClient;
  }

  public void recordEvent(CustomEvents event, Map<String, String> attributes) {
    String[] tags =
        attributes.entrySet().stream()
            .map(entry -> entry.getKey() + ":" + entry.getValue())
            .toArray(String[]::new);

    Event ddEvent =
        Event.builder()
            .withTitle(event.getEventName())
            .withText(attributes.toString())
            .withAlertType(Event.AlertType.INFO)
            .build();

    statsDClient.recordEvent(ddEvent, tags);
  }
}
