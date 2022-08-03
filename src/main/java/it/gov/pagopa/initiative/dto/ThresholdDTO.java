package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ThresholdDTO {
    @JsonProperty("from")
    private String from;

    @JsonProperty("fromIncluded")
    private Boolean fromIncluded;

    @JsonProperty("to")
    private String to;

    @JsonProperty("toIncluded")
    private Boolean toIncluded;
}
