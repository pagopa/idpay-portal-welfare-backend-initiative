package it.gov.pagopa.initiative.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
@Document("initiative")
public class Initiative {

    @Id
    private String initiativeId;
    private String initiativeName;
    private String organizationId;
    private String serviceId;
    private String pdndToken;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    private String status;
    private Boolean pdndCheck;
    private Boolean autocertificationCheck;
    private Boolean beneficiaryRanking;
    private InitiativeGeneral general;
    private InitiativeAdditional additionalInfo;
    private InitiativeBeneficiaryRule beneficiaryRule;
    private InitiativeLegal legal;

}
