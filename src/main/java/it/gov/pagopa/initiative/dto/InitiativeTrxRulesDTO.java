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
public class InitiativeTrxRulesDTO {
    @JsonProperty("threshold")
    private ThresholdDTO threshold;

    @JsonProperty("mccFilter")
    private MccFilterDTO mccFilter;

    @JsonProperty("trxCount")
    private TrxCountDTO trxCount;

    @JsonProperty("rewardLimit")
    @Valid
    private List<RewardLimitDTO> rewardLimit;

    @JsonProperty("daysOfWeekAllowed")
    @Valid
    private List<DaysOfWeekAllowedItemDTO> daysOfWeekAllowed;
}
