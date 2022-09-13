package it.gov.pagopa.initiative.dto.rule.refund;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.gov.pagopa.initiative.utils.constraint.RefundRuleType;
import it.gov.pagopa.initiative.utils.validator.ValidationOnGroup;
import lombok.*;

import javax.validation.Valid;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@RefundRuleType(groups = ValidationOnGroup.class)
public class InitiativeRefundRuleDTO {
    @Valid
    @JsonProperty("accumulatedAmount")
    private AccumulatedAmountDTO accumulatedAmount;

    @Valid
    @JsonProperty("timeParameter")
    private TimeParameterDTO timeParameter;

    @Valid
    @JsonProperty("additionalInfo")
    private RefundAdditionalInfoDTO additionalInfo;
}
