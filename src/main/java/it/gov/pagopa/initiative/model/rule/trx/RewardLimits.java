package it.gov.pagopa.initiative.model.rule.trx;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class RewardLimits {
    private RewardLimitFrequency frequency;

    private Long rewardLimitCents;

    public enum RewardLimitFrequency {
        DAILY,
        WEEKLY,
        MONTHLY,
        YEARLY
    }
}
