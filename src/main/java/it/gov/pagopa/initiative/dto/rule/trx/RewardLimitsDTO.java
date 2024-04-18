package it.gov.pagopa.initiative.dto.rule.trx;


import it.gov.pagopa.initiative.utils.validator.ValidationApiEnabledGroup;
import lombok.*;

import jakarta.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class RewardLimitsDTO {

    @NotNull(groups = ValidationApiEnabledGroup.class)
    private RewardLimitFrequency frequency;

    @NotNull(groups = ValidationApiEnabledGroup.class)
    private Long rewardLimitCents;

    public enum RewardLimitFrequency {
        DAILY,
        WEEKLY,
        MONTHLY,
        YEARLY
    }

}
