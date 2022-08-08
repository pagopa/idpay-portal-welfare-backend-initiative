package it.gov.pagopa.initiative.event;

import it.gov.pagopa.initiative.dto.InitiativeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class InitiativeProducer {

  @Value("${spring.cloud.stream.bindings.initiativeQueue-out-0.binder}")
  private String initiativeBinder;

  @Autowired
  StreamBridge streamBridge;

  public void sendPublishInitiative(InitiativeDTO initiative){
//    streamBridge.send("initiativeQueue-out-0", initiativeDTO);
    streamBridge.send("initiativeQueue-out-0", initiativeBinder, initiative);
  }

}
