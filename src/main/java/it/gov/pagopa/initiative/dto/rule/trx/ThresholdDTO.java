package it.gov.pagopa.initiative.dto.rule.trx;

import it.gov.pagopa.initiative.utils.constraint.ThresholdFromToValue;
import it.gov.pagopa.initiative.utils.validator.ValidationOnGroup;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@ThresholdFromToValue(from = "from", to = "to", groups = ValidationOnGroup.class)
public class ThresholdDTO {
    @NotNull(groups = ValidationOnGroup.class)
    @Min(value = 0, message = "from must be at least 0", groups = ValidationOnGroup.class)
    private BigDecimal from;

    @NotNull(groups = ValidationOnGroup.class)
    private Boolean fromIncluded;

    @NotNull(groups = ValidationOnGroup.class)
    @Min(value = 1, message = "to must be at least 1", groups = ValidationOnGroup.class)
    private BigDecimal to;

    @NotNull(groups = ValidationOnGroup.class)
    private Boolean toIncluded;
}
