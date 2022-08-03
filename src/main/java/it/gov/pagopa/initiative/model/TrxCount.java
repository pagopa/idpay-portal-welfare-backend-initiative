package it.gov.pagopa.initiative.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class TrxCount {
    private BigDecimal from;

    private Boolean fromIncluded;

    private BigDecimal to;

    private Boolean toIncluded;
}
