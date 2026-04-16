package it.gov.pagopa.initiative.model;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class SelfCriteriaText implements ISelfDeclarationCriteria {

  private TypeTextEnum typeTextEnum;
  private String description;
  private String subDescription;
  private String value;
  private String code;
  
}
