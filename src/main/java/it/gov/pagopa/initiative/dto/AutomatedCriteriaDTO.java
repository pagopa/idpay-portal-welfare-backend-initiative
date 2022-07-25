package it.gov.pagopa.initiative.dto;

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

  @NotBlank
  private String code;

  @NotBlank
  private String field;

  @NotBlank
  private String operator;

  @NotBlank
  private String value;
}
