package it.gov.pagopa.initiative.dto.rule.reward;

import it.gov.pagopa.initiative.utils.validator.ValidationOnGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RewardValueDTO implements InitiativeRewardRuleDTO {
    @Min(value = 0, message = "Reward value must be at least 0%", groups = ValidationOnGroup.class)
    @Max(value = 100, message = "Reward value must be at most 100%", groups = ValidationOnGroup.class)
    @NotNull(groups = ValidationOnGroup.class)
    private BigDecimal rewardValue;
}
