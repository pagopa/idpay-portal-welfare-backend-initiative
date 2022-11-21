package it.gov.pagopa.initiative.model;

import it.gov.pagopa.initiative.constants.InitiativeConstants;
import it.gov.pagopa.initiative.model.rule.refund.InitiativeRefundRule;
import it.gov.pagopa.initiative.model.rule.reward.InitiativeRewardRule;
import it.gov.pagopa.initiative.model.rule.trx.InitiativeTrxConditions;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Document("initiative")
public class Initiative {

    @Id
    private String initiativeId;
    private String initiativeName;
    private String organizationId;
    private String organizationName;
    private String organizationVat;
    @CreatedBy
    private String createdBy;
    @LastModifiedBy
    private String updatedBy;
    @CreatedDate
    private LocalDateTime creationDate;
    @LastModifiedDate
    private LocalDateTime updateDate;
    private String pdndToken;
    private String status;
    private InitiativeGeneral general;
    private InitiativeAdditional additionalInfo;
    private InitiativeBeneficiaryRule beneficiaryRule;
    private InitiativeRewardRule rewardRule;
    private InitiativeTrxConditions trxRule;
    private InitiativeRefundRule refundRule;
    private Boolean enabled;

    public static final String getLogoURL(String organizationId, String initiativeId){
        return InitiativeConstants.Logo.URL_LOGO+String.format(InitiativeConstants.Logo.LOGO_PATH_TEMPLATE, organizationId,initiativeId, InitiativeConstants.Logo.LOGO_NAME);
    }
}
