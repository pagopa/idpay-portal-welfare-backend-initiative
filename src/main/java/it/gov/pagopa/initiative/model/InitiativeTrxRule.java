package it.gov.pagopa.initiative.model;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class InitiativeTrxRule {
    private Threshold threshold;
    private MccFilter mccFilter;
    private TrxCount trxCount;
    private List<RewardLimit> rewardLimit;
    private List<DaysOfWeekAllowedItem> daysOfWeekAllowed;
}
