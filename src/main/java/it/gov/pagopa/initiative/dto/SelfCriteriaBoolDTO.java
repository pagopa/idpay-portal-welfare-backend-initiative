package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.gov.pagopa.initiative.utils.validator.ValidationOnGroup;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * SelfCriteriaBoolDTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SelfCriteriaBoolDTO implements AnyOfInitiativeBeneficiaryRuleDTOSelfDeclarationCriteriaItems {

  @JsonProperty("_type")
  @NotNull(groups = ValidationOnGroup.class)
  private TypeBoolEnum type;

  @JsonProperty("description")
  @NotBlank(groups = ValidationOnGroup.class)
  private String description;

  @JsonProperty("value")
  @NotNull(groups = ValidationOnGroup.class)
  private Boolean value;

  @JsonProperty("code")
  @NotNull(groups = ValidationOnGroup.class)
  private String code;

}
