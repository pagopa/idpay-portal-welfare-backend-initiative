package it.gov.pagopa.initiative.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.gov.pagopa.initiative.dto.DaysOfWeekDTO;
import it.gov.pagopa.initiative.dto.IntervalsDTO;
import lombok.*;

import javax.validation.Valid;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class DaysOfWeekAllowedItem {
    private List<DaysOfWeek> daysOfWeek;

    private List<Intervals> intervals;
}
