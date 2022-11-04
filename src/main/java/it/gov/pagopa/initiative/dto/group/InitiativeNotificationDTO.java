package it.gov.pagopa.initiative.dto.group;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InitiativeNotificationDTO {

    private String initiativeName;
    private String serviceId;
}
