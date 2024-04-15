package it.gov.pagopa.initiative.dto.rule.trx;


import it.gov.pagopa.initiative.utils.validator.ValidationApiEnabledGroup;
import jakarta.annotation.PostConstruct;
import lombok.*;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class RewardLimitsDTO {

    @NotNull(groups = ValidationApiEnabledGroup.class)
    private RewardLimitFrequency frequency;

    @NotNull(groups = ValidationApiEnabledGroup.class)
    private BigDecimal rewardLimit;

    public enum RewardLimitFrequency {
        DAILY,
        WEEKLY,
        MONTHLY,
        YEARLY
    }

    @PostConstruct
    private void postConstruct(){
        this.rewardLimit = this.rewardLimit.multiply(BigDecimal.valueOf(100));
    }
}
