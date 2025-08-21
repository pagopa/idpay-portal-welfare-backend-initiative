package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.gov.pagopa.initiative.utils.validator.ValidationApiEnabledGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

/**
 * SelfCriteriaMultiDTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SelfCriteriaMultiConsentDTO implements AnyOfInitiativeBeneficiaryRuleDTOSelfDeclarationCriteriaItems {

  @JsonProperty("_type")
  @NotNull(groups = ValidationApiEnabledGroup.class)
  private TypeMultiConsentEnum type;

  @JsonProperty("description")
  @NotBlank(groups = ValidationApiEnabledGroup.class)
  private String description;

  @JsonProperty("subDescription")
  //@NotBlank(groups = ValidationApiEnabledGroup.class)
  private String subDescription;


  @JsonProperty("value")
  @NotEmpty(groups = ValidationApiEnabledGroup.class)
  private List<SelfCriteriaMultiConsentValueDTO> value;

  @JsonProperty("code")
  @NotNull(groups = ValidationApiEnabledGroup.class)
  private String code;




}
