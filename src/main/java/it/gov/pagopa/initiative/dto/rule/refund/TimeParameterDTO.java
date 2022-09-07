package it.gov.pagopa.initiative.dto.rule.refund;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import it.gov.pagopa.initiative.utils.validator.ValidationOnGroup;
import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TimeParameterDTO {
    public enum TimeTypeEnum {
        CLOSED("CLOSED"),

        DAILY("DAILY"),

        WEEKLY("WEEKLY"),

        MONTHLY("MONTHLY"),

        QUARTERLY("QUARTERLY");

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

    @NotNull(groups = ValidationOnGroup.class)
    @JsonProperty("timeType")
    private TimeTypeEnum timeType;
}
