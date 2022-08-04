package it.gov.pagopa.initiative.dto.rule.trx;

import it.gov.pagopa.initiative.utils.validator.ValidationOnGroup;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class InitiativeTrxConditionsDTO {
    @NotEmpty(groups = ValidationOnGroup.class)
    private DayOfWeekDTO daysOfWeek;
    private ThresholdDTO threshold;
    private MccFilterDTO mccFilter;
    private TrxCountDTO trxCount;
    @Size(min = 1, groups = ValidationOnGroup.class, message = "Reward limit must contain at least 1 element.")
    private List<RewardLimitsDTO> rewardLimits;
}
