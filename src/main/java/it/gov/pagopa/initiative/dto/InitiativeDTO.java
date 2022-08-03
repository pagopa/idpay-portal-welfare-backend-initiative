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

  @JsonProperty("creationDate")
  private LocalDateTime creationDate;

  @JsonProperty("updateDate")
  private LocalDateTime updateDate;

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

  @JsonProperty("rewardRule")
  private InitiativeRewardRuleTypeItems rewardRule;

  @JsonProperty("trxRule")
  private InitiativeTrxRulesDTO trxRule;

  @JsonProperty("legal")
  private InitiativeLegalDTO legal;

}
