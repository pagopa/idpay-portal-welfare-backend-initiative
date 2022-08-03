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
public class DaysOfWeekDTO {
    public enum DaysEnum {
        MONDAY("Monday"),

        TUESDAY("Tuesday"),

        WEDNESDAY("Wednesday"),

        THURSDAY("Thursday"),

        FRIDAY("Friday"),

        SATURDAY("Saturday"),

        SUNDAY("Sunday");

        private String value;

        DaysEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static DaysEnum fromValue(String text) {
            for (DaysEnum b : DaysEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("days")
    private DaysEnum days;
}
