package it.gov.pagopa.initiative.model;

import it.gov.pagopa.initiative.dto.InitiativeDTO;
import it.gov.pagopa.initiative.model.rule.refund.InitiativeRefundRule;
import it.gov.pagopa.initiative.model.rule.reward.InitiativeRewardRule;
import it.gov.pagopa.initiative.model.rule.trx.InitiativeTrxConditions;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@FieldNameConstants
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
    private String status;
    private InitiativeGeneral general;
    private InitiativeAdditional additionalInfo;
    private InitiativeBeneficiaryRule beneficiaryRule;
    private InitiativeDTO.InitiativeRewardTypeEnum initiativeRewardType;
    private InitiativeRewardRule rewardRule;
    private InitiativeTrxConditions trxRule;
    private InitiativeRefundRule refundRule;
    private Boolean enabled;

}
