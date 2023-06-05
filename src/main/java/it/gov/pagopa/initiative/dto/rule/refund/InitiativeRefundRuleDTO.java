package it.gov.pagopa.initiative.dto.rule.refund;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.gov.pagopa.initiative.dto.InitiativeOrganizationInfoDTO;
import it.gov.pagopa.initiative.utils.constraint.RefundRuleType;
import it.gov.pagopa.initiative.utils.validator.ValidationApiEnabledGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.validation.Valid;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
@RefundRuleType(groups = ValidationApiEnabledGroup.class)
public class InitiativeRefundRuleDTO extends InitiativeOrganizationInfoDTO {
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
