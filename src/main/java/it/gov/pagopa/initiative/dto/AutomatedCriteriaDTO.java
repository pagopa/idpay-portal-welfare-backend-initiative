package it.gov.pagopa.initiative.dto;

import it.gov.pagopa.initiative.utils.validator.ValidationOff;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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

  @NotBlank(groups = ValidationOff.class)
  private String code;

  @NotBlank(groups = ValidationOff.class)
  private String field;

  @NotBlank(groups = ValidationOff.class)
  private String operator;

  @NotBlank(groups = ValidationOff.class)
  private String value;
}
