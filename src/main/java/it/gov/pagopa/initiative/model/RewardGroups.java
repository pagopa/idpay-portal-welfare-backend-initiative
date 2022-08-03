package it.gov.pagopa.initiative.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import it.gov.pagopa.initiative.dto.RewardGroupsDTO;
import it.gov.pagopa.initiative.dto.RewardGroupsItemDTO;
import lombok.*;

import javax.validation.Valid;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class RewardGroups implements IRewardRuleTypeItems {
    public enum TypeEnum {
        REWARDGROUPS("rewardGroups");

        private String value;

        TypeEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static RewardGroups.TypeEnum fromValue(String text) {
            for (RewardGroups.TypeEnum b : RewardGroups.TypeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    private TypeEnum _type;

    private List<RewardGroupsItem> rewardGroups;
}
