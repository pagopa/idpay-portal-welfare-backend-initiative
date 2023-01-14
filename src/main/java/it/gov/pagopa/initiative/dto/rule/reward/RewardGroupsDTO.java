package it.gov.pagopa.initiative.dto.rule.reward;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.gov.pagopa.initiative.utils.constraint.RewardGroupFromToValue;
import it.gov.pagopa.initiative.utils.validator.ValidationApiEnabledGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@RewardGroupFromToValue(listOfGroupValue = "rewardGroups", groups = ValidationApiEnabledGroup.class)
public class RewardGroupsDTO implements InitiativeRewardRuleDTO {

    @NotNull(groups = ValidationApiEnabledGroup.class)
    @JsonProperty("_type")
    private String type;

    @Valid
    private List<RewardGroupDTO> rewardGroups;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RewardGroupDTO {

        @Min(value = 0, message = "from must be at least 0", groups = ValidationApiEnabledGroup.class)
        private BigDecimal from;

        @Min(value = 1, message = "to must be at least 1", groups = ValidationApiEnabledGroup.class)
        private BigDecimal to;

        @Min(value = 0, message = "Reward value must be at least 0%", groups = ValidationApiEnabledGroup.class)
        @Max(value = 100, message = "Reward value must be at most 100%", groups = ValidationApiEnabledGroup.class)
        private BigDecimal rewardValue;
    }
}
