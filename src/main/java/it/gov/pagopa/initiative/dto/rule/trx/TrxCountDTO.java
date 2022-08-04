package it.gov.pagopa.initiative.dto.rule.trx;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class TrxCountDTO {
    private Long from;
    private boolean fromIncluded;

    private Long to;
    private boolean toIncluded;
}
