package it.gov.pagopa.initiative.dto;

import it.gov.pagopa.initiative.dto.rule.refund.InitiativeRefundRuleDTO;
import it.gov.pagopa.initiative.dto.rule.reward.InitiativeRewardRuleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InitiativeDetailDTO {
    private String initiativeName;
    private String status;
    private String description;
    private String ruleDescription;
    private Instant onboardingStartDate;
    private Instant onboardingEndDate;
    private Instant fruitionStartDate;
    private Instant fruitionEndDate;
    private InitiativeRewardRuleDTO rewardRule;
    private InitiativeRefundRuleDTO refundRule;
    private String privacyLink;
    private String tcLink;
    private String logoURL;
    private Instant updateDate;
    private String serviceId;
}
