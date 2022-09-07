package it.gov.pagopa.initiative.model.rule.refund;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class InitiativeRefundRule {
    private AccumulatedAmount accumulatedAmount;

    private TimeParameter timeParameter;

    private AdditionalInfo additionalInfo;
}
