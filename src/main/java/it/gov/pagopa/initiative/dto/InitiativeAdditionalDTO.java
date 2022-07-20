package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Size;

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
public class InitiativeAdditionalDTO   {

  @Size(min = 2, message = "At least 2 characters")
  @JsonProperty("serviceId")
  private String serviceId;

  @Size(min = 2, message = "At least 2 characters")
  @JsonProperty("serviceName")
  private String serviceName;

  @Size(min = 2, message = "At least 2 characters")
  @JsonProperty("argument")
  private String argument;

  @Size(min = 2, message = "At least 2 characters")
  @JsonProperty("description")
  private String description;

}
