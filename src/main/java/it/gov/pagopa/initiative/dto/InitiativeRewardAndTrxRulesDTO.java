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
public class InitiativeRewardAndTrxRulesDTO {
    @JsonProperty("rewardRule")
    private InitiativeRewardRuleTypeItems rewardRule;

    @JsonProperty("trxRule")
    private InitiativeTrxRulesDTO trxRule;
}
