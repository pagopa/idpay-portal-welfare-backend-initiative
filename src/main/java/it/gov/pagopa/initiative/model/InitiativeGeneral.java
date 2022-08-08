package it.gov.pagopa.initiative.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class InitiativeGeneral   {

  private BigDecimal budget;
  private BeneficiaryTypeEnum beneficiaryType;
  private Boolean beneficiaryKnown;
  private BigDecimal beneficiaryBudget;
  private LocalDate startDate;
  private LocalDate endDate;
  private LocalDate rankingStartDate;
  private LocalDate rankingEndDate;

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

}
