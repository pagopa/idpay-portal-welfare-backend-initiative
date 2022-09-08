package it.gov.pagopa.initiative.model.rule.refund;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class AccumulatedAmount {
    public enum AccumulatedTypeEnum {
        BUDGET_EXHAUSTED("BUDGET_EXHAUSTED"),

        THRESHOLD_REACHED("THRESHOLD_REACHED");

        private String value;

        AccumulatedTypeEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static AccumulatedTypeEnum fromValue(String text) {
            for (AccumulatedTypeEnum b : AccumulatedTypeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    private AccumulatedTypeEnum accomulatedType;

    private BigDecimal refundThreshold;
}