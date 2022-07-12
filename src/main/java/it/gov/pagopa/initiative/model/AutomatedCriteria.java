package it.gov.pagopa.initiative.model;

import lombok.*;
import org.springframework.validation.annotation.Validated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class AutomatedCriteria {

  private String authority;
  private String code ;
  private Boolean field;
  private String operator;
  private String value;

}
