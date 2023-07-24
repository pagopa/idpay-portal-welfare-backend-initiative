package it.gov.pagopa.initiative.dto;

import it.gov.pagopa.initiative.dto.rule.refund.InitiativeRefundRuleDTO;
import it.gov.pagopa.initiative.dto.rule.reward.InitiativeRewardRuleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InitiativeDetailDTO {
    private String initiativeName;
    private String status;
    private String description;
    private String ruleDescription;
    private LocalDate onboardingStartDate;
    private LocalDate onboardingEndDate;
    private LocalDate fruitionStartDate;
    private LocalDate fruitionEndDate;
    private InitiativeRewardRuleDTO rewardRule;
    private InitiativeRefundRuleDTO refundRule;
    private String privacyLink;
    private String tcLink;
    private String logoURL;
    private LocalDateTime updateDate;
    private String serviceId;
}
