package it.gov.pagopa.initiative.dto.rule.trx;

import it.gov.pagopa.initiative.utils.constraint.TrxCountFromToValue;
import it.gov.pagopa.initiative.utils.validator.ValidationApiEnabledGroup;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@TrxCountFromToValue(groups = ValidationApiEnabledGroup.class)
public class TrxCountDTO {

    @NotNull(groups = ValidationApiEnabledGroup.class)
    @Min(value = 1, message = "from must be at least 1", groups = ValidationApiEnabledGroup.class)
    private Long from;

    @NotNull(groups = ValidationApiEnabledGroup.class)
    private Boolean fromIncluded;

    @Nullable
    private Long to;
    @Nullable
    private Boolean toIncluded;
}
