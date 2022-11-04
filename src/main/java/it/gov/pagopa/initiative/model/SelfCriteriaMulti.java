package it.gov.pagopa.initiative.model;

import lombok.*;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class SelfCriteriaMulti implements ISelfDeclarationCriteria {

  private TypeMultiEnum _type;
  private String description;
  private List<String> value;
  private String code;
  
}
