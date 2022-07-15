package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

/**
 * SelfCriteriaMultiDTO
 */
@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class SelfCriteriaMultiDTO implements AnyOfInitiativeBeneficiaryRuleDTOSelfDeclarationCriteriaItems {

  @JsonProperty("_type")
  private TypeEnum _type;

  @JsonProperty("description")
  private String description;

  @JsonProperty("value")
  @Valid
  private List<String> value;

  @JsonProperty("code")
  private String code;

}
