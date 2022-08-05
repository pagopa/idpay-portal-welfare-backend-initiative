package it.gov.pagopa.initiative.dto.rule.trx;

import it.gov.pagopa.initiative.utils.constraint.TrxCountFromToValue;
import it.gov.pagopa.initiative.utils.validator.ValidationOnGroup;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@TrxCountFromToValue(from = "from", to = "to", groups = ValidationOnGroup.class)
public class TrxCountDTO {

    @NotNull(groups = ValidationOnGroup.class)
    @Min(value = 1, message = "from must be at least 1", groups = ValidationOnGroup.class)
    private Long from;

    @NotNull(groups = ValidationOnGroup.class)
    private Boolean fromIncluded;

    @NotNull(groups = ValidationOnGroup.class)
    @Min(value = 1, message = "to must be at least 1", groups = ValidationOnGroup.class)
    private Long to;

    @NotNull(groups = ValidationOnGroup.class)
    private Boolean toIncluded;
}
