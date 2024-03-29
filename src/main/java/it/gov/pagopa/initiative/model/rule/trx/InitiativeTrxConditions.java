package it.gov.pagopa.initiative.model.rule.trx;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class InitiativeTrxConditions {
    private DayOfWeek daysOfWeek;
    private Threshold threshold;
    private MccFilter mccFilter;
    private TrxCount trxCount;
    private List<RewardLimits> rewardLimits;
}
