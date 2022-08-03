package it.gov.pagopa.initiative.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class Threshold {
    private String from;

    private Boolean fromIncluded;

    private String to;

    private Boolean toIncluded;
}
