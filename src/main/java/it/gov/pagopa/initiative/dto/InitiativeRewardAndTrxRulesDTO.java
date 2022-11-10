package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.gov.pagopa.initiative.dto.rule.reward.InitiativeRewardRuleDTO;
import it.gov.pagopa.initiative.dto.rule.trx.InitiativeTrxConditionsDTO;
import it.gov.pagopa.initiative.utils.validator.ValidationOnGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
public class InitiativeRewardAndTrxRulesDTO extends InitiativeOrganizationInfoDTO {

    @JsonProperty("rewardRule")
    @Valid
    @NotNull(groups = ValidationOnGroup.class)
    private InitiativeRewardRuleDTO rewardRule;

    @JsonProperty("trxRule")
    @Valid
    @NotNull(groups = ValidationOnGroup.class)
    private InitiativeTrxConditionsDTO trxRule;
}
