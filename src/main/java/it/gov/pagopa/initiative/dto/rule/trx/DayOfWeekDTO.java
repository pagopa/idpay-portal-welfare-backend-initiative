package it.gov.pagopa.initiative.dto.rule.trx;

import com.fasterxml.jackson.annotation.JsonFormat;
import it.gov.pagopa.initiative.utils.constraint.DayConfigNotRepeatedIntervalsConstraint;
import it.gov.pagopa.initiative.utils.constraint.DayOfWeekStartTimeBeforeEndTime;
import it.gov.pagopa.initiative.utils.validator.ValidationApiEnabledGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DayOfWeekDTO extends ArrayList<DayOfWeekDTO.DayConfig> {

    public DayOfWeekDTO(){
        super();
    }

    public DayOfWeekDTO(List<DayConfig> list){
        super(list);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @DayConfigNotRepeatedIntervalsConstraint(groups = ValidationApiEnabledGroup.class)
    public static class DayConfig {
        @NotNull(groups = ValidationApiEnabledGroup.class)
        @NotEmpty(groups = ValidationApiEnabledGroup.class)
        private Set<DayOfWeek> daysOfWeek;

        @Valid
        @NotNull(groups = ValidationApiEnabledGroup.class)
        @NotEmpty(groups = ValidationApiEnabledGroup.class)
        private List<Interval> intervals;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @DayOfWeekStartTimeBeforeEndTime(groups = ValidationApiEnabledGroup.class)
    public static class Interval {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss[.SSS]")
        @NotNull(groups = ValidationApiEnabledGroup.class)
        private LocalTime startTime;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss[.SSS]")
        @NotNull(groups = ValidationApiEnabledGroup.class)
        private LocalTime endTime;
    }
}
