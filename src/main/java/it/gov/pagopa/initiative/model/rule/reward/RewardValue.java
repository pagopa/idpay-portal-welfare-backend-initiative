package it.gov.pagopa.initiative.model.rule.reward;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("_type")
    private String type;
    private String rewardValueType;
    private BigDecimal rewardValue;
}
