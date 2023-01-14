package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.gov.pagopa.initiative.utils.validator.ValidationApiEnabledGroup;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
public class SelfCriteriaMultiDTO implements AnyOfInitiativeBeneficiaryRuleDTOSelfDeclarationCriteriaItems {

  @JsonProperty("_type")
  @NotNull(groups = ValidationApiEnabledGroup.class)
  private TypeMultiEnum type;

  @JsonProperty("description")
  @NotBlank(groups = ValidationApiEnabledGroup.class)
  private String description;

  @JsonProperty("value")
  @NotEmpty(groups = ValidationApiEnabledGroup.class)
  private List<String> value;

  @JsonProperty("code")
  @NotNull(groups = ValidationApiEnabledGroup.class)
  private String code;

}
