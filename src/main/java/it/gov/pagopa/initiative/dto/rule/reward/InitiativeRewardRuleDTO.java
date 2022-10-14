package it.gov.pagopa.initiative.dto.rule.reward;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "_type",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RewardGroupsDTO.class, name = "rewardGroups"),
        @JsonSubTypes.Type(value = RewardValueDTO.class, name = "rewardValue"),
})
public interface InitiativeRewardRuleDTO {
}
