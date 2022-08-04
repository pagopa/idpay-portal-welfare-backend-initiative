package it.gov.pagopa.initiative.model.rule.trx;

import lombok.*;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class MccFilter {
    private boolean allowedList;
    private Set<String> values;
}
