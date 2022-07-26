package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import it.gov.pagopa.initiative.utils.constraint.BeneficiaryBudgetValue;
import it.gov.pagopa.initiative.utils.constraint.RankingAndSpendingDatesDoubleUseCaseValue;
import it.gov.pagopa.initiative.utils.validator.ValidationOff;
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
@ToString
@Builder
@BeneficiaryBudgetValue(budget1 = "beneficiaryBudget", budget2 = "budget", groups = ValidationOff.class)
@RankingAndSpendingDatesDoubleUseCaseValue(date1 = "rankingStartDate", date2 = "rankingEndDate", date3 = "startDate", date4 = "endDate", groups = ValidationOff.class)
public class InitiativeGeneralDTO   {

  @JsonProperty("budget")
  @Min(value = 0, message = "budget should have an amount of at least 1", groups = ValidationOff.class)
  @NotNull(groups = ValidationOff.class)
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
  @NotNull(groups = ValidationOff.class)
  private BeneficiaryTypeEnum beneficiaryType;

  @JsonProperty("beneficiaryKnown")
  @NotNull(groups = ValidationOff.class)
  private Boolean beneficiaryKnown;

  @JsonProperty("beneficiaryBudget")
  @Min(value = 0, message = "Beneficiary budget should have an amount of at least 1", groups = ValidationOff.class)
  @NotNull(groups = ValidationOff.class)
  private BigDecimal beneficiaryBudget;

  //La validazione di startDate e endDate viene effettuata da RankingAndSpendingDatesDoubleUseCaseValue, dovranno essere successive alle date di ranking.
  @JsonProperty("startDate")
  private LocalDate startDate;

  @JsonProperty("endDate")
  private LocalDate endDate;

  @JsonProperty("rankingStartDate")
  @FutureOrPresent(groups = ValidationOff.class)
  private LocalDate rankingStartDate;

  @JsonProperty("rankingEndDate")
  @Future(groups = ValidationOff.class)
  private LocalDate rankingEndDate;

}
