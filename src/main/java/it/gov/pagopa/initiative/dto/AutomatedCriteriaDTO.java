package it.gov.pagopa.initiative.dto;

import lombok.*;
import org.springframework.validation.annotation.Validated;

/**
 * AutomatedCriteriaDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-07-10T13:24:21.794Z[GMT]")

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
