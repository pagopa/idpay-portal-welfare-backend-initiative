package it.gov.pagopa.initiative.dto.rule.trx;

import it.gov.pagopa.initiative.utils.validator.ValidationApiEnabledGroup;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class MccFilterDTO {

    @NotNull(groups = ValidationApiEnabledGroup.class)
    private boolean allowedList;

    @NotNull(groups = ValidationApiEnabledGroup.class)
    @NotEmpty(groups = ValidationApiEnabledGroup.class)
    private Set<String> values;
}
