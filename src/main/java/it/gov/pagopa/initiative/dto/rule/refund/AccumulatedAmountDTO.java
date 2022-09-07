package it.gov.pagopa.initiative.dto.rule.refund;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import it.gov.pagopa.initiative.utils.constraint.AccumulatedAmountType;
import it.gov.pagopa.initiative.utils.validator.ValidationOnGroup;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
@AccumulatedAmountType(value1 = "accumulatedType", value2 = "refundThreshold", groups = ValidationOnGroup.class)
public class AccumulatedAmountDTO {
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

    @NotNull(groups = ValidationOnGroup.class)
    @JsonProperty("accumulatedType")
    private AccumulatedTypeEnum accumulatedType;

    @Min(value = 0, groups = ValidationOnGroup.class)
    @JsonProperty("refundThreshold")
    private BigDecimal refundThreshold;
}
