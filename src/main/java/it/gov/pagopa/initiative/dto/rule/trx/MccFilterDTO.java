package it.gov.pagopa.initiative.dto.rule.trx;

import lombok.*;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class MccFilterDTO {
    private boolean allowedList;
    private Set<String> values;
}
