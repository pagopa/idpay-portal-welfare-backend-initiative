package it.gov.pagopa.initiative.model.rule.reward;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RewardValue implements InitiativeRewardRule {
    private String type;
    private BigDecimal rewardValue;
}
