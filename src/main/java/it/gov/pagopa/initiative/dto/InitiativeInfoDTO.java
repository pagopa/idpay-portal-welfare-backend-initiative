package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.gov.pagopa.initiative.utils.validator.ValidationOnGroup;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * InitiativePatchDTO
 */
@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class InitiativeInfoDTO {

  @JsonProperty("general")
  @Valid
  @NotNull(groups = ValidationOnGroup.class)
  private InitiativeGeneralDTO general;

  @JsonProperty("additionalInfo")
  @Valid
//  @NotNull
  private InitiativeAdditionalDTO additionalInfo;

}
