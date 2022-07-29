package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.gov.pagopa.initiative.utils.validator.ValidationOnGroup;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InitiativeAdditionalDTO   {

  @JsonProperty("serviceId")
  private String serviceId;

  @JsonProperty("serviceName")
  private String serviceName;

  @JsonProperty("argument")
  @NotBlank(groups = ValidationOnGroup.class)
  private String argument;

  @JsonProperty("description")
  @NotBlank(groups = ValidationOnGroup.class)
  private String description;

  @JsonProperty("channels")
  @NotNull(groups = ValidationOnGroup.class)
  private List<@NotNull(groups = ValidationOnGroup.class) ChannelDTO> channels;

}
