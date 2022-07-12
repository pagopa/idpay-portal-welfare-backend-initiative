package it.gov.pagopa.initiative.dto.request.header;

import lombok.Data;

import java.util.List;

@Data
public class OrganizationRequestTokenDTO {

    private List<RolesRequestTokenDTO> roles;
    private String id;
    private String fiscal_code;

}
