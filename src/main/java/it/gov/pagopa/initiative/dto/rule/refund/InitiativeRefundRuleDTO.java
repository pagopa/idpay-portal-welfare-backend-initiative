package it.gov.pagopa.initiative.dto.rule.refund;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InitiativeRefundRuleDTO {
    @JsonProperty("accumulatedAmount")
    private AccomulatedAmountDTO accumulatedAmount;

    @JsonProperty("timeParameter")
    private TimeParameterDTO timeParameter;

    @JsonProperty("additionalInfo")
    private AdditionalInfoDTO additionalInfo;
}
