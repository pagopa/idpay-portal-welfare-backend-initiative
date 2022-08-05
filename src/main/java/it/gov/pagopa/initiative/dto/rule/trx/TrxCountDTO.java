package it.gov.pagopa.initiative.dto.rule.trx;

import it.gov.pagopa.initiative.utils.validator.ValidationOnGroup;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class TrxCountDTO {

    @NotNull(groups = ValidationOnGroup.class)
    private Long from;

    @NotNull(groups = ValidationOnGroup.class)
    private Boolean fromIncluded;

    @NotNull(groups = ValidationOnGroup.class)
    private Long to;

    @NotNull(groups = ValidationOnGroup.class)
    private Boolean toIncluded;
}
