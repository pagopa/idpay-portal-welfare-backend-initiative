package it.gov.pagopa.model;

import it.gov.pagopa.dto.InitiativeBeneficiaryRuleDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document("initiative")
public class Initiative {

    @Id
    private String initiativeId;
    private String initiativeName;
    private String organizationId;
    private String serviceId;
    private String pdndToken;
    private String status;
    private Boolean pdndCheck;
    private Boolean autocertificationCheck;
    private Boolean beneficiaryRanking;
    private InitiativeGeneral general;
    private InitiativeBeneficiaryRule beneficiaryRule;
    private InitiativeLegal legal;

}
