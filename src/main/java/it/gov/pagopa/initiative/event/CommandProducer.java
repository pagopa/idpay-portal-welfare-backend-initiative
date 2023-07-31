package it.gov.pagopa.initiative.event;

import it.gov.pagopa.initiative.dto.QueueCommandOperationDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CommandProducer {

    private static final String COMMAND_QUEUE_OUT_0 = "commandQueue-out-0";

    @Autowired
    StreamBridge streamBridge;

    public boolean sendCommand(QueueCommandOperationDTO queueCommandOperationDTO){
        log.debug("Sending Command Operation to {}", COMMAND_QUEUE_OUT_0);
        return streamBridge.send(COMMAND_QUEUE_OUT_0, queueCommandOperationDTO);
    }

}
