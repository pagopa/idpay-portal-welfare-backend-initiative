package it.gov.pagopa.initiative.dto.io.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class OrganizationDTO {
    @JsonProperty("name")
    private String organizationName;
    @JsonProperty("fiscal_code")
    private String organizationFiscalCode;
    @JsonProperty("department_name")
    private String departmentName;
}
