package it.gov.pagopa.initiative.dto.rule.reward;

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
public class RewardGroupsDTO implements InitiativeRewardRuleDTO {
    private List<RewardGroupDTO> rewardGroups;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RewardGroupDTO {
        private BigDecimal from;
        private BigDecimal to;
        private BigDecimal rewardValue;
    }
}
