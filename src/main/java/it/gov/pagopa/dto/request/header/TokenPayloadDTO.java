package it.gov.pagopa.dto.request.header;

import lombok.Data;

@Data
public class TokenPayloadDTO {

    private String uid;
    private String name;;
    private String family_name;
    private String email;
    private OrganizationRequestTokenDTO organizationRequestTokenDTO;

}

