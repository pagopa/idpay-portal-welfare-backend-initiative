package it.gov.pagopa.initiative.model.rule.trx;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Threshold {
    private Long fromCents;
    private boolean fromIncluded;

    private Long toCents;
    private boolean toIncluded;
}
