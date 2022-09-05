package it.gov.pagopa.initiative.model.rule.refund;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.gov.pagopa.initiative.dto.rule.refund.AccomulatedAmountDTO;
import it.gov.pagopa.initiative.dto.rule.refund.AdditionalInfoDTO;
import it.gov.pagopa.initiative.dto.rule.refund.TimeParameterDTO;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class InitiativeRefundRule {
    private AccomulatedAmount accumulatedAmount;

    private TimeParameter timeParameter;

    private AdditionalInfo additionalInfo;
}
