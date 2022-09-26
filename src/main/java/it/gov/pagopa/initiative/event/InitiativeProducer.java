package it.gov.pagopa.initiative.event;

import it.gov.pagopa.initiative.dto.InitiativeDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InitiativeProducer {

  private static final String INITIATIVE_QUEUE_OUT_0 = "initiativeQueue-out-0";

  @Autowired
  StreamBridge streamBridge;

  public boolean sendPublishInitiative(InitiativeDTO initiativeDTO){
    log.debug("Sending Initiative to {}", INITIATIVE_QUEUE_OUT_0);
    return streamBridge.send(INITIATIVE_QUEUE_OUT_0, initiativeDTO);
  }

}
