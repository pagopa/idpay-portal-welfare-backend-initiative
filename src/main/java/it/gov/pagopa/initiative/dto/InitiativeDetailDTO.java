package it.gov.pagopa.initiative.dto;

import it.gov.pagopa.initiative.dto.rule.refund.InitiativeRefundRuleDTO;
import it.gov.pagopa.initiative.dto.rule.reward.InitiativeRewardRuleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InitiativeDetailDTO {
    private String initiativeId;
    private String initiativeName;
    private String status;
    private String description;
    private LocalDate endDate;
    private InitiativeRewardRuleDTO rewardRule;
    private InitiativeRefundRuleDTO refundRule;
    private String privacyLink;
    private String tcLink;
    private String logoFileName;
}
