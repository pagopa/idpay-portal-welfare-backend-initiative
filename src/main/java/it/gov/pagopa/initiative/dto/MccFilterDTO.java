package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

import javax.validation.Valid;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MccFilterDTO {
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
        public static FilterOperatorEnum fromValue(String text) {
            for (FilterOperatorEnum b : FilterOperatorEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("filterOperator")
    private FilterOperatorEnum filterOperator;

    @JsonProperty("value")
    @Valid
    private List<String> value;
}
