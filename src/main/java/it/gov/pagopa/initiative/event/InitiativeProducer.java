package it.gov.pagopa.initiative.event;

import it.gov.pagopa.initiative.model.Initiative;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InitiativeProducer {

  private static final String INITIATIVE_QUEUE_OUT_0 = "initiativeQueue-out-0";

  private final StreamBridge streamBridge;

  public InitiativeProducer(StreamBridge streamBridge) {
    this.streamBridge = streamBridge;
  }

  public boolean sendPublishInitiative(Initiative initiative){
    log.debug("Sending Initiative to {}", INITIATIVE_QUEUE_OUT_0);
    return streamBridge.send(INITIATIVE_QUEUE_OUT_0, initiative);
  }
}
