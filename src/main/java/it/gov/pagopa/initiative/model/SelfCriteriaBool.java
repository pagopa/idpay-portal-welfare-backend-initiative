package it.gov.pagopa.initiative.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class SelfCriteriaBool implements ISelfDeclarationCriteria {

  @JsonProperty("_type")
  private TypeBoolEnum _type;
  private String description;
  private String subDescription;
  private Boolean value;
  private String code;

}
