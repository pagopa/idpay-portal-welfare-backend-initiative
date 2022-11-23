package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import it.gov.pagopa.initiative.utils.constraint.BeneficiaryBudgetValue;
import it.gov.pagopa.initiative.utils.constraint.RankingAndSpendingDatesDoubleUseCaseValue;
import it.gov.pagopa.initiative.utils.constraint.initiative.general.RankingEnabledNotNullForBeneficiaryKnownFalseConstraint;
import it.gov.pagopa.initiative.utils.constraint.initiative.general.RankingGracePeriodConstraint;
import it.gov.pagopa.initiative.utils.validator.ValidationOnGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

/**
 * InitiativeGeneralDTO
 */
//@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@BeneficiaryBudgetValue(budget1 = "beneficiaryBudget", budget2 = "budget", groups = ValidationOnGroup.class)
@RankingAndSpendingDatesDoubleUseCaseValue(date1 = "rankingStartDate", date2 = "rankingEndDate", date3 = "startDate", date4 = "endDate", groups = ValidationOnGroup.class)
@RankingGracePeriodConstraint(groups = ValidationOnGroup.class)
@RankingEnabledNotNullForBeneficiaryKnownFalseConstraint(groups = ValidationOnGroup.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InitiativeGeneralDTO extends InitiativeOrganizationInfoDTO {

    @JsonProperty("budget")
    @Min(value = 1, message = "budget should have an amount of at least 1", groups = ValidationOnGroup.class)
    @NotNull(groups = ValidationOnGroup.class)
    private BigDecimal budget;

    /**
     * Gets or Sets beneficiaryType
     */
    public enum BeneficiaryTypeEnum {
        PF("PF"), PG("PG");

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

    @JsonProperty("beneficiaryType")
    @NotNull(groups = ValidationOnGroup.class)
    private BeneficiaryTypeEnum beneficiaryType;

    @JsonProperty("beneficiaryKnown")
    @NotNull(groups = ValidationOnGroup.class)
    private Boolean beneficiaryKnown;

    @JsonProperty("beneficiaryBudget")
    @Min(value = 1, message = "Beneficiary budget should have an amount of at least 1", groups = ValidationOnGroup.class)
    @NotNull(groups = ValidationOnGroup.class)
    private BigDecimal beneficiaryBudget;

    /**
     * Start of period of spending funds in an initiative
     */
    @JsonProperty("startDate")
    @NotNull(groups = ValidationOnGroup.class)
    private LocalDate startDate;

    /**
     * End of period of spending funds in an initiative
     */
    @JsonProperty("endDate")
    @NotNull(groups = ValidationOnGroup.class)
    private LocalDate endDate;

    /**
     * Start of period of participation/adhesion in an initiative
     */
    @JsonProperty("rankingStartDate")
    @FutureOrPresent(groups = ValidationOnGroup.class)
    private LocalDate rankingStartDate;

    /**
     * End of period of participation/adhesion in an initiative
     */
    @JsonProperty("rankingEndDate")
    @Future(groups = ValidationOnGroup.class)
    private LocalDate rankingEndDate;

    @JsonProperty("rankingEnabled")
    private Boolean rankingEnabled;

    @JsonProperty("descriptionMap")
    @Valid
//    @NotEmpty(groups = ValidationOnGroup.class)
    private Map<String, String> descriptionMap;
}
