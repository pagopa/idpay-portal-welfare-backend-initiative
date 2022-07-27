package it.gov.pagopa.initiative.dto;

import it.gov.pagopa.initiative.utils.validator.ValidationOnGroup;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

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

  @NotBlank(groups = ValidationOnGroup.class)
  private String code;

  @NotBlank(groups = ValidationOnGroup.class)
  private String field;

  @NotBlank(groups = ValidationOnGroup.class)
  private String operator;

  @NotBlank(groups = ValidationOnGroup.class)
  private String value;
}
