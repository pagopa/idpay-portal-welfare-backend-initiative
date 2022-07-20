package it.gov.pagopa.initiative.dto;

import lombok.*;
import org.springframework.validation.annotation.Validated;

/**
 * AutomatedCriteriaDTO
 */
@Validated
@Data @AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class AutomatedCriteriaDTO   {

  private String authority;

  private String code ;

  private Boolean field;

  private String operator;

  private String value;
}
