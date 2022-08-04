package it.gov.pagopa.initiative.model.rule.trx;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class TrxCount {
    private Long from;
    private boolean fromIncluded;

    private Long to;
    private boolean toIncluded;
}
