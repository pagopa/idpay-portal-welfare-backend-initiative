package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * InitiativeAdditionalDTO
 */
@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class InitiativeAdditionalDTO   {

  @NotBlank
  @JsonProperty("serviceId")
  private String serviceId;

  @NotBlank
  @JsonProperty("serviceName")
  private String serviceName;

  @NotBlank
  @JsonProperty("argument")
  private String argument;

  @NotBlank
  @JsonProperty("description")
  private String description;

  @JsonProperty("channels")
  private List<ChannelDTO> channels;

}
