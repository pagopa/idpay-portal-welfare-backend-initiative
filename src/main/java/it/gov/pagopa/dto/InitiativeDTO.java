package it.gov.pagopa.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

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
  private InitiativeAdditionalDTO additionalInfo = null;

  @JsonProperty("beneficiaryRule")
  private InitiativeBeneficiaryRuleDTO beneficiaryRule;

  @JsonProperty("legal")
  private InitiativeLegalDTO legal;

}
