package it.gov.pagopa.initiative.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class SelfCriteriaMulti implements ISelfDeclarationCriteria {

  @JsonProperty("_type")
  private TypeMultiEnum _type;
  private String description;
  private String subDescription;
  private List<String> value;
  private String code;
  
}
