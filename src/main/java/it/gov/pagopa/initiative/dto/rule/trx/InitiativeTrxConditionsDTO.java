package it.gov.pagopa.initiative.dto.rule.trx;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class InitiativeTrxConditionsDTO {
    private DayOfWeekDTO daysOfWeek;
    private ThresholdDTO threshold;
    private MccFilterDTO mccFilter;
    private TrxCountDTO trxCount;
    private List<RewardLimitsDTO> rewardLimits;
}
