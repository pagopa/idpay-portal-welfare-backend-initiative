package it.gov.pagopa.initiative.model;

import lombok.*;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class SelfCriteriaMulti implements ISelfDeclarationCriteria {

  private TypeEnum _type;
  private String description;
  private List<String> value;
  private String code;
  
}
