package it.gov.pagopa.initiative.model.rule.refund;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import it.gov.pagopa.initiative.dto.rule.refund.TimeParameterDTO;
import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class TimeParameter {
    public enum TimeTypeEnum {
        FINISHEDINITIATIVE("FinishedInitiative"),

        DAILY("Daily"),

        WEEKLY("Weekly"),

        MONTHLY("Monthly"),

        EVERYTHREEMONTH("EveryThreeMonth");

        private String value;

        TimeTypeEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static TimeTypeEnum fromValue(String text) {
            for (TimeTypeEnum b : TimeTypeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    private TimeTypeEnum timeType;
}
