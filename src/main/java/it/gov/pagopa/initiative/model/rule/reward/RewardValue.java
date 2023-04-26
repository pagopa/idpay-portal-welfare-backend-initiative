package it.gov.pagopa.initiative.model.rule.reward;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
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
    private RewardValueTypeEnum rewardValueType;
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
        public static RewardValue.RewardValueTypeEnum fromValue(String text) {
            for (RewardValue.RewardValueTypeEnum b : RewardValue.RewardValueTypeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }
}
