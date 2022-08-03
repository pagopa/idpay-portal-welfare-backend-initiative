package it.gov.pagopa.initiative.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import it.gov.pagopa.initiative.dto.DaysOfWeekDTO;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class DaysOfWeek {
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
        public static DaysOfWeek.DaysEnum fromValue(String text) {
            for (DaysOfWeek.DaysEnum b : DaysOfWeek.DaysEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    private DaysEnum days;

}
