package it.gov.pagopa.initiative.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class QueueCommandOperationDTO {

    private String operationType;
    private String entityId;
    private LocalDateTime operationTime;
    private String organizationId;

}
