package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TrxCountDTO {
    @JsonProperty("from")
    private BigDecimal from;

    @JsonProperty("fromIncluded")
    private Boolean fromIncluded;

    @JsonProperty("to")
    private BigDecimal to;

    @JsonProperty("toIncluded")
    private Boolean toIncluded;
}
