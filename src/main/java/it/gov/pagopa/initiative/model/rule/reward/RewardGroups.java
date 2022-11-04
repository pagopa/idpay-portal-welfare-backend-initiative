package it.gov.pagopa.initiative.model.rule.reward;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RewardGroups implements InitiativeRewardRule {
    @JsonProperty("_type")
    private String type;
    private List<RewardGroup> rewardGroups;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RewardGroup {
        private BigDecimal from;
        private BigDecimal to;
        private BigDecimal rewardValue;
    }
}
