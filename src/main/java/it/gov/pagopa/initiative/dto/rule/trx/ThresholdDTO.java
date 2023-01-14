package it.gov.pagopa.initiative.dto.rule.trx;

import it.gov.pagopa.initiative.utils.constraint.ThresholdFromToValue;
import it.gov.pagopa.initiative.utils.validator.ValidationApiEnabledGroup;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@ThresholdFromToValue(groups = ValidationApiEnabledGroup.class)
public class ThresholdDTO {
    @NotNull(groups = ValidationApiEnabledGroup.class)
    @Min(value = 0, message = "from must be at least 0", groups = ValidationApiEnabledGroup.class)
    private BigDecimal from;

    @NotNull(groups = ValidationApiEnabledGroup.class)
    private Boolean fromIncluded;

    @NotNull(groups = ValidationApiEnabledGroup.class)
    @Min(value = 1, message = "to must be at least 1", groups = ValidationApiEnabledGroup.class)
    private BigDecimal to;

    @NotNull(groups = ValidationApiEnabledGroup.class)
    private Boolean toIncluded;
}
