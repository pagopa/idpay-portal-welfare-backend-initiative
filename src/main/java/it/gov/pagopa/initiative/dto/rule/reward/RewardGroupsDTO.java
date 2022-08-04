package it.gov.pagopa.initiative.dto.rule.reward;

import it.gov.pagopa.initiative.utils.constraint.RewardGroupFromToValue;
import it.gov.pagopa.initiative.utils.validator.ValidationOnGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@RewardGroupFromToValue(listOfGroupValue = "rewardGroups", groups = ValidationOnGroup.class)
public class RewardGroupsDTO implements InitiativeRewardRuleDTO {
    @Valid
    private List<RewardGroupDTO> rewardGroups;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RewardGroupDTO {
        private BigDecimal from;
        private BigDecimal to;

        @Min(value = 0, message = "Reward value must be at least 0%", groups = ValidationOnGroup.class)
        @Max(value = 100, message = "Reward value must be at most 100%", groups = ValidationOnGroup.class)
        private BigDecimal rewardValue;
    }
}
