package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.validation.annotation.Validated;

/**
 * InitiativeDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-07-10T13:24:21.794Z[GMT]")

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InitiativeDTO   {

  @JsonProperty("initiativeId")
  private String initiativeId;

  @JsonProperty("initiativeName")
  private String initiativeName;

  @JsonProperty("organizationId")
  private String organizationId;

  @JsonProperty("pdndToken")
  private String pdndToken;

  @JsonProperty("status")
  private String status;

  @JsonProperty("pdndCheck")
  private Boolean pdndCheck;

  @JsonProperty("autocertificationCheck")
  private Boolean autocertificationCheck;

  @JsonProperty("beneficiaryRanking")
  private Boolean beneficiaryRanking;

  @JsonProperty("general")
  private InitiativeGeneralDTO general;

  @JsonProperty("additionalInfo")
  private InitiativeAdditionalDTO additionalInfo;

  @JsonProperty("beneficiaryRule")
  private InitiativeBeneficiaryRuleDTO beneficiaryRule;

  @JsonProperty("legal")
  private InitiativeLegalDTO legal;

}
