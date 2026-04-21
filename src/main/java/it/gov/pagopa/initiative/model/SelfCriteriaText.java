package it.gov.pagopa.initiative.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class SelfCriteriaText implements ISelfDeclarationCriteria {

  @JsonProperty("_type")
  private TypeTextEnum _type;
  private String description;
  private String subDescription;
  private String value;
  private String code;
  
}
