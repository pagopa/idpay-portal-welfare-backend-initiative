package it.gov.pagopa.initiative.dto.rule.refund;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TimeParameterDTO {
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

    @NotNull
    @JsonProperty("timeType")
    private TimeTypeEnum timeType;
}
