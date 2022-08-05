package it.gov.pagopa.initiative.dto.rule.trx;

import it.gov.pagopa.initiative.utils.validator.ValidationOnGroup;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class ThresholdDTO {
    @NotNull(groups = ValidationOnGroup.class)
    private BigDecimal from;

    @NotNull(groups = ValidationOnGroup.class)
    private Boolean fromIncluded;

    @NotNull(groups = ValidationOnGroup.class)
    private BigDecimal to;

    @NotNull(groups = ValidationOnGroup.class)
    private Boolean toIncluded;
}
