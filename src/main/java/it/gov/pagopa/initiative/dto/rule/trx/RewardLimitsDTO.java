package it.gov.pagopa.initiative.dto.rule.trx;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class RewardLimitsDTO {
    private RewardLimitFrequency frequency;

    private BigDecimal rewardLimit;

    public enum RewardLimitFrequency {
        DAILY,
        WEEKLY,
        MONTHLY,
        YEARLY
    }
}
