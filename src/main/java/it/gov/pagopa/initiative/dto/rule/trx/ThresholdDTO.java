package it.gov.pagopa.initiative.dto.rule.trx;

import it.gov.pagopa.initiative.utils.validator.ValidationOnGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ThresholdDTO {
    @NotNull(groups = ValidationOnGroup.class)
    private BigDecimal from;

    @NotNull(groups = ValidationOnGroup.class)
    private boolean fromIncluded;

    @NotNull(groups = ValidationOnGroup.class)
    private BigDecimal to;

    @NotNull(groups = ValidationOnGroup.class)
    private boolean toIncluded;
}
