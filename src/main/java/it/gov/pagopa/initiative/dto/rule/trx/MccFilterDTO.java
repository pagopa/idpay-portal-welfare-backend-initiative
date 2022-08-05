package it.gov.pagopa.initiative.dto.rule.trx;

import it.gov.pagopa.initiative.utils.validator.ValidationOnGroup;
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

    @NotNull(groups = ValidationOnGroup.class)
    private boolean allowedList;

    @NotNull(groups = ValidationOnGroup.class)
    @NotEmpty(groups = ValidationOnGroup.class)
    private Set<String> values;
}
