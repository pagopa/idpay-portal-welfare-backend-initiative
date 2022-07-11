package it.gov.pagopa.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.validation.annotation.Validated;

/**
 * SelfCriteriaBoolDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-07-10T13:24:21.794Z[GMT]")

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class SelfCriteriaBoolDTO  implements AnyOfInitiativeBeneficiaryRuleDTOSelfDeclarationCriteriaItems {

  @JsonProperty("_type")
  private TypeEnum _type;

  @JsonProperty("description")
  private String description;

  @JsonProperty("value")
  private Boolean value;

  @JsonProperty("code")
  private String code;

}
