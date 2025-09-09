package it.gov.pagopa.initiative.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

import java.time.LocalDate;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class InitiativeGeneral {

    private Long budgetCents;
    private BeneficiaryTypeEnum beneficiaryType;
    private String familyUnitComposition;
    private Boolean beneficiaryKnown;
    private Long beneficiaryBudgetCents;
    private Long beneficiaryBudgetMaxCents;
    /**
     * Start of period of spending funds in an initiative
     */
    private LocalDate startDate;
    /**
     * End of period of spending funds in an initiative
     */
    private LocalDate endDate;
    /**
     * Start of period of participation/adhesion in an initiative
     */
    private LocalDate rankingStartDate;
    /**
     * End of period of participation/adhesion in an initiative
     */
    private LocalDate rankingEndDate;
    private Boolean rankingEnabled;
    private Map<String, String> descriptionMap;

    /**
     * Gets or Sets beneficiaryType
     */
    public enum BeneficiaryTypeEnum {

        PF("PF"), PG("PG"), NF("NF");

        private final String value;

        BeneficiaryTypeEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static BeneficiaryTypeEnum fromValue(String text) {
            for (BeneficiaryTypeEnum b : BeneficiaryTypeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }
}
