package it.gov.pagopa.initiative.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class MccFilter {
    public enum FilterOperatorEnum {
        INCLUDE("include"),

        EXCLUDE("exclude");

        private String value;

        FilterOperatorEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static MccFilter.FilterOperatorEnum fromValue(String text) {
            for (MccFilter.FilterOperatorEnum b : MccFilter.FilterOperatorEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    private FilterOperatorEnum filterOperator;

    private List<String> value;
}
