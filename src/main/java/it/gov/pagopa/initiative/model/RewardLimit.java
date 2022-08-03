package it.gov.pagopa.initiative.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class RewardLimit {
    public enum FrequencyEnum {
        DAILY("Daily"),

        MONTHLY("Monthly"),

        ANNUALY("Annualy");

        private String value;

        FrequencyEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static RewardLimit.FrequencyEnum fromValue(String text) {
            for (RewardLimit.FrequencyEnum b : RewardLimit.FrequencyEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    private FrequencyEnum frequency;

    private String rewardLimit;
}
