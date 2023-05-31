package it.gov.pagopa.initiative.dto.rule.trx;

import it.gov.pagopa.initiative.utils.constraint.TrxCountFromToValue;
import it.gov.pagopa.initiative.utils.validator.ValidationApiEnabledGroup;
import lombok.*;

import jakarta.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@TrxCountFromToValue(groups = ValidationApiEnabledGroup.class)
public class TrxCountDTO {

    @Min(value = 1, message = "from must be at least 1", groups = ValidationApiEnabledGroup.class)
    private Long from;
    private boolean fromIncluded;

    @Min(value = 1, message = "to must be at least 1", groups = ValidationApiEnabledGroup.class)
    private Long to;
    private boolean toIncluded;
}
