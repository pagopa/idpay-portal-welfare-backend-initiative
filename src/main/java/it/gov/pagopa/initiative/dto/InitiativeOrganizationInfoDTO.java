package it.gov.pagopa.initiative.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class InitiativeOrganizationInfoDTO {
    private String organizationName;
    private String organizationVat;
    private String organizationUserId;
    private String organizationUserRole;
}
