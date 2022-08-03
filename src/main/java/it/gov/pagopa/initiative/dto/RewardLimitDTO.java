package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RewardLimitDTO {
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
        public static FrequencyEnum fromValue(String text) {
            for (FrequencyEnum b : FrequencyEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("frequency")
    private FrequencyEnum frequency;

    @JsonProperty("rewardLimit")
    private String rewardLimit;
}
