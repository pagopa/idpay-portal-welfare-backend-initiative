package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.gov.pagopa.initiative.model.InitiativeGeneral;
import it.gov.pagopa.initiative.utils.validator.ValidationApiEnabledGroup;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.Map;

/**
 * InitiativeDTO
 */
@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InitiativeMilDTO {

  @JsonProperty("initiativeId")
  private String initiativeId;

  @JsonProperty("initiativeName")
  private String initiativeName;

  @JsonProperty("organizationId")
  private String organizationId;

  @JsonProperty("organizationName")
  private String organizationName;

  @JsonProperty("descriptionMap")
  @Valid
  @NotEmpty(groups = ValidationApiEnabledGroup.class)
  private Map<String, String> descriptionMap;

  @JsonProperty("fruitionStartDate")
  @NotNull(groups = ValidationApiEnabledGroup.class)
  private LocalDate fruitionStartDate;

  @JsonProperty("fruitionEndDate")
  @NotNull(groups = ValidationApiEnabledGroup.class)
  private LocalDate fruitionEndDate;

  @JsonProperty("initiativeRewardType")
  private InitiativeDTO.InitiativeRewardTypeEnum initiativeRewardType;

  @JsonProperty("beneficiaryType")
  private InitiativeGeneral.BeneficiaryTypeEnum beneficiaryType;

  @JsonProperty("rankingEnabled")
  private Boolean rankingEnabled;

  @JsonProperty("onboardingStartDate")
  @FutureOrPresent(groups = ValidationApiEnabledGroup.class)
  private LocalDate onboardingStartDate;

  @JsonProperty("onboardingEndDate")
  @Future(groups = ValidationApiEnabledGroup.class)
  private LocalDate onboardingEndDate;

  @JsonProperty("beneficiaryKnown")
  private Boolean beneficiaryKnown;

  @JsonProperty("status")
  private String status;

  @JsonProperty("tcLink")
  private String tcLink;

  @JsonProperty("privacyLink")
  private String privacyLink;

  @JsonProperty("logoURL")
  private String logoURL;

}
