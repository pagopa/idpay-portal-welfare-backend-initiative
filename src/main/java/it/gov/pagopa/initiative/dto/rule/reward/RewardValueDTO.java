package it.gov.pagopa.initiative.dto.rule.reward;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.gov.pagopa.initiative.utils.validator.ValidationApiEnabledGroup;
import javax.validation.constraints.Pattern;
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

    @NotNull(groups = ValidationApiEnabledGroup.class)
    @JsonProperty("_type")
    private String type;

    @Pattern(regexp = "PERCENTAGE|ABSOLUTE", flags = Pattern.Flag.CASE_INSENSITIVE)
    @NotNull(groups = ValidationApiEnabledGroup.class)
    private String rewardValueType;

    @Min(value = 0, message = "Reward value must be at least 0", groups = ValidationApiEnabledGroup.class)
    @NotNull(groups = ValidationApiEnabledGroup.class)
    private BigDecimal rewardValue;
}
