package it.gov.pagopa.initiative.model.rule.refund;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import it.gov.pagopa.initiative.dto.rule.refund.AccomulatedAmountDTO;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class AccomulatedAmount {
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

    private AccomulatedTypeEnum accomulatedType;

    private BigDecimal refundThreshold;
}
