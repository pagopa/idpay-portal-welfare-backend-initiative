package it.gov.pagopa.initiative.dto;

import java.time.LocalDate;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

import lombok.*;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * InitiativeGeneralDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-07-10T13:24:21.794Z[GMT]")

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class InitiativeGeneralDTO   {

  /*@Size(min = 2, message = "At least 2 characters") //TODO lunghezza nome
  @JsonProperty("name")
  private String name;*/

  @Min(value = 1000000, message = "budget should be at least 1000000")
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
  @Min(value = 1, message = "beneficiaryBudget should be at least 1")
  //TODO @Max(value = 1000, message = "beneficiaryBudget should not be greater than 1000")
  private BigDecimal beneficiaryBudget;




  @JsonProperty("startDate")
  @FutureOrPresent
  private LocalDate startDate;

  @JsonProperty("endDate")
  @Future
  private LocalDate endDate;

  @JsonProperty("rankingStartDate")
  private LocalDate rankingStartDate;

  @JsonProperty("rankingEndDate")
  private LocalDate rankingEndDate;

}
