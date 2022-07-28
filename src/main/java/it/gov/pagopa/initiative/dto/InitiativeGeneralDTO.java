package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import it.gov.pagopa.initiative.utils.constraint.BeneficiaryBudgetValue;
import it.gov.pagopa.initiative.utils.constraint.RankingAndSpendingDatesDoubleUseCaseValue;
import it.gov.pagopa.initiative.utils.validator.ValidationOnGroup;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * InitiativeGeneralDTO
 */
@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@BeneficiaryBudgetValue(budget1 = "beneficiaryBudget", budget2 = "budget", groups = ValidationOnGroup.class)
@RankingAndSpendingDatesDoubleUseCaseValue(date1 = "rankingStartDate", date2 = "rankingEndDate", date3 = "startDate", date4 = "endDate", groups = ValidationOnGroup.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InitiativeGeneralDTO   {

  @JsonProperty("budget")
  @Min(value = 0, message = "budget should have an amount of at least 1", groups = ValidationOnGroup.class)
  @NotNull(groups = ValidationOnGroup.class)
  private BigDecimal budget;

  /**
   * Gets or Sets beneficiaryType
   */
  public enum BeneficiaryTypeEnum {
    PF("PF"),
    PG("PG");

    private String value;

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
  @Min(value = 0, message = "Beneficiary budget should have an amount of at least 0", groups = ValidationOnGroup.class)
  @NotNull(groups = ValidationOnGroup.class)
  private BigDecimal beneficiaryBudget;

  @JsonProperty("startDate")
  private LocalDate startDate;

  @JsonProperty("endDate")
  private LocalDate endDate;

  @JsonProperty("rankingStartDate")
  @FutureOrPresent(groups = ValidationOnGroup.class)
  private LocalDate rankingStartDate;

  @JsonProperty("rankingEndDate")
  @Future(groups = ValidationOnGroup.class)
  private LocalDate rankingEndDate;

}
