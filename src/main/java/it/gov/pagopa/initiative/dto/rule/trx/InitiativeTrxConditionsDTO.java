package it.gov.pagopa.initiative.dto.rule.trx;

import it.gov.pagopa.initiative.utils.validator.ValidationApiEnabledGroup;
import lombok.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class InitiativeTrxConditionsDTO {
    @Valid
    private DayOfWeekDTO daysOfWeek;

    @Valid
    private ThresholdDTO threshold;

    @Valid
    private TrxCountDTO trxCount;

    @Valid
    private MccFilterDTO mccFilter;

    @Valid
    @Size(groups = ValidationApiEnabledGroup.class, message = "Reward limit must contain at least 1 element.")
    private List<RewardLimitsDTO> rewardLimits; //TODO Validatore custom per verificare che non ci siano elementi uguali??
}
