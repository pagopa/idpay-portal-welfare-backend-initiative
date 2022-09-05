package it.gov.pagopa.initiative.dto.rule.refund;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccomulatedAmountDTO {
    public enum AccomulatedTypeEnum {
        PAYMENTOUT("PaymentOut"),

        REACHINGTHRESHOLD("ReachingThreshold");

        private String value;

        AccomulatedTypeEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static AccomulatedTypeEnum fromValue(String text) {
            for (AccomulatedTypeEnum b : AccomulatedTypeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @NotNull
    @JsonProperty("accomulatedType")
    private AccomulatedTypeEnum accomulatedType;

    @JsonProperty("refundThreshold")
    private BigDecimal refundThreshold;
}
