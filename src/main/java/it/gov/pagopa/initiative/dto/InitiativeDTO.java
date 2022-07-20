package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * InitiativeDTO
 */
@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InitiativeDTO   {

  //@NotEmpty
  //@NotBlank //TODO controllare se esegue stesse validazioni di notnull. da ripetere per tutti gli attributi
  //TODO @NotNull
  //TODO @Size(min = 2, message = "At least 2 characters")
  @JsonProperty("initiativeId")
  private String initiativeId;

  //@NotEmpty
  //@NotBlank
  // TODO @NotNull
 // @Size(min = 2, message = "At least 2 characters")
  @JsonProperty("initiativeName")
  private String initiativeName;

//  @NotEmpty
//  @NotBlank
//  @NotNull
 // @Size(min = 2, message = "At least 2 characters")
  @JsonProperty("organizationId")
  private String organizationId;

//  @NotEmpty
//  @NotBlank
//  @NotNull
//  @Size(min = 2, message = "At least 2 characters")
  @JsonProperty("pdndToken")
  private String pdndToken;

  @JsonProperty("creationDate")
  private LocalDateTime creationDate;

  @JsonProperty("updateDate")
  private LocalDateTime updateDate;

//  @NotEmpty
//  @NotBlank
//  @NotNull
//  @Size(min = 2, message = "At least 2 characters")
  @JsonProperty("status")
  private String status;


  @JsonProperty("pdndCheck")
  //@NotNull
  //TODO @AssertTrue
  private Boolean pdndCheck;

  @JsonProperty("autocertificationCheck")
 // @NotNull
  //TODO @AssertTrue
  private Boolean autocertificationCheck;

  @JsonProperty("beneficiaryRanking")
 // @NotNull
  private Boolean beneficiaryRanking;

//  @NotNull
//  @Valid
  @JsonProperty("general")
  private InitiativeGeneralDTO general;

//  @NotNull
//  @Valid
  @JsonProperty("additionalInfo")
  private InitiativeAdditionalDTO additionalInfo;

//  @NotNull
//  @Valid
  @JsonProperty("beneficiaryRule")
  private InitiativeBeneficiaryRuleDTO beneficiaryRule;

//  @NotNull
//  @Valid
  @JsonProperty("legal")
  private InitiativeLegalDTO legal;

}
