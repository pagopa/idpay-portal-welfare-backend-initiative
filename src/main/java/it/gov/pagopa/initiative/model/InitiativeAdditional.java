package it.gov.pagopa.initiative.model;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * InitiativeAdditionalDTO
 */
@Validated
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
  private List<Channel> channels;

}
