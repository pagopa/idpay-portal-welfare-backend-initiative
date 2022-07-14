package it.gov.pagopa.initiative.model;

import lombok.*;
import org.springframework.validation.annotation.Validated;

/**
 * InitiativeAdditionalDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-07-11T11:28:33.400Z[GMT]")

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class InitiativeAdditional {

  private String serviceId;
  private String serviceName;
  private String argument;
  private String description;

}
