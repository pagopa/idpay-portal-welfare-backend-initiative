package it.gov.pagopa.initiative.model;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class SelfCriteriaBool implements ISelfDeclarationCriteria {

  private TypeBoolEnum _type;
  private String description;
  private Boolean value;
  private String code;

}
