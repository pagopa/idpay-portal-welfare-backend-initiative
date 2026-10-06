package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.gov.pagopa.initiative.utils.validator.ValidationApiEnabledGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

// BND-1882 / BND-1883: informative self declaration criteria (dto side) - codes ANPR and ADE
/**
 * SelfCriteriaInformativeDTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SelfCriteriaInformativeDTO implements AnyOfInitiativeBeneficiaryRuleDTOSelfDeclarationCriteriaItems {

  @JsonProperty("_type")
  @NotNull(groups = ValidationApiEnabledGroup.class)
  private TypeInformativeEnum type;

  @JsonProperty("code")
  @NotBlank(groups = ValidationApiEnabledGroup.class)
  private String code;

  @JsonProperty("description")
  @NotBlank(groups = ValidationApiEnabledGroup.class)
  private String description;

  @JsonProperty("organization")
  @NotBlank(groups = ValidationApiEnabledGroup.class)
  private String organization;

  @JsonProperty("value")
  @NotBlank(groups = ValidationApiEnabledGroup.class)
  private String value;

}


