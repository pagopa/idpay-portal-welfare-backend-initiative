package it.gov.pagopa.initiative.model.rule.trx;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class RewardLimits {
    private RewardLimitFrequency frequency;

    private BigDecimal rewardLimit;

    public enum RewardLimitFrequency {
        DAILY,
        WEEKLY,
        MONTHLY,
        YEARLY
    }
}
