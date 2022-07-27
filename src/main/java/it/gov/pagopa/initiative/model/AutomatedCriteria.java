package it.gov.pagopa.initiative.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class AutomatedCriteria {

  private String authority;
  private String code ;
  private String field;
  private String operator;
  private String value;

}
