package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.Valid;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DaysOfWeekAllowedItemDTO {
    @JsonProperty("daysOfWeek")
    @Valid
    private List<DaysOfWeekDTO> daysOfWeek;

    @JsonProperty("intervals")
    @Valid
    private List<IntervalsDTO> intervals;
}
