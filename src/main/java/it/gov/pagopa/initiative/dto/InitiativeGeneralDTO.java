package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import it.gov.pagopa.initiative.utils.constraint.*;
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
@BeneficiaryBudgetValue(value1 = "beneficiaryBudget", value2 = "budget")
//@StartDateLessThanEndDate(value1 = "startDate", value2 = "endDate")
//@RankingEndDateLessThanStartDate(value1 = "rankingEndDate", value2 = "startDate")
//@RankingStartDateLessThanRankingEndDate(value1 = "rankingStartDate", value2 = "rankingEndDate")
@DisjointSetOrderedDatesFieldsFromLowestToHighest(orderedDates = {"rankingStartDate", "rankingEndDate", "startDate", "endDate"})
public class InitiativeGeneralDTO   {

  /*@Size(min = 2, message = "At least 2 characters") //TODO lunghezza nome
  @JsonProperty("name")
  private String name;*/

  @Min(value = 2, message = "budget should have an amount of at least 2")
  //TODO impostare max?
  @JsonProperty("budget")
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
  @NotNull
  private BeneficiaryTypeEnum beneficiaryType;

  @JsonProperty("beneficiaryKnown")
  @NotNull
  private Boolean beneficiaryKnown;

  @JsonProperty("beneficiaryBudget")
  @Min(value = 1, message = "Beneficiary budget should have an amount of at least 1")
  //TODO @Max(value = 1000, message = "beneficiaryBudget should not be greater than 1000")
  private BigDecimal beneficiaryBudget;

  @JsonProperty("startDate")
  private LocalDate startDate;

  @JsonProperty("endDate")
  private LocalDate endDate;

  @JsonProperty("rankingStartDate")
  @FutureOrPresent
  private LocalDate rankingStartDate;

  @JsonProperty("rankingEndDate")
  @Future
  private LocalDate rankingEndDate;

}
