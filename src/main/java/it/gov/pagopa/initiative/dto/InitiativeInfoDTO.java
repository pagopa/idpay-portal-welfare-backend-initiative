package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * InitiativePatchDTO
 */
@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class InitiativeInfoDTO {

  @JsonProperty("general")
  @Valid
  @NotNull
  private InitiativeGeneralDTO general;

  @JsonProperty("additionalInfo")
  @Valid
//  @NotNull
  private InitiativeAdditionalDTO additionalInfo;

}
