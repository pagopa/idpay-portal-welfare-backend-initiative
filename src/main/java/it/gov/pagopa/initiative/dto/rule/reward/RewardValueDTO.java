package it.gov.pagopa.initiative.dto.rule.reward;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import it.gov.pagopa.initiative.utils.validator.ValidationApiEnabledGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RewardValueDTO implements InitiativeRewardRuleDTO {

    @NotNull(groups = ValidationApiEnabledGroup.class)
    @JsonProperty("_type")
    private String type;
    @JsonProperty("rewardValueType")
    @NotNull(groups = ValidationApiEnabledGroup.class)
    private RewardValueTypeEnum rewardValueType;

    @Min(value = 0, message = "Reward value must be at least 0", groups = ValidationApiEnabledGroup.class)
    @NotNull(groups = ValidationApiEnabledGroup.class)
    private BigDecimal rewardValue;

    /**
     * Gets or Sets RewardValueType
     */
    public enum RewardValueTypeEnum {
        ABSOLUTE("ABSOLUTE"), PERCENTAGE("PERCENTAGE");

        private final String value;

        RewardValueTypeEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static RewardValueTypeEnum fromValue(String text) {
            for (RewardValueTypeEnum b : RewardValueTypeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }
}
