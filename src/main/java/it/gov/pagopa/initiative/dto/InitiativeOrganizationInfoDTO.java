package it.gov.pagopa.initiative.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@SuperBuilder
public class InitiativeOrganizationInfoDTO {
    private String organizationName;
    private String organizationVat;
//    private String organizationUserId;
    private String organizationUserRole;
}
