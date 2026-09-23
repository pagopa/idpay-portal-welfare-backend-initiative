package it.gov.pagopa.initiative.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


// BND-1882 / BND-1883: informative self declaration criteria (model side) - handles codes ANPR and ADE
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class SelfCriteriaInformative implements ISelfDeclarationCriteria {

  @JsonProperty("_type")
  private TypeInformativeEnum _type;
  private String code;
  private String description;
  private String organization;
  private String value;

}


