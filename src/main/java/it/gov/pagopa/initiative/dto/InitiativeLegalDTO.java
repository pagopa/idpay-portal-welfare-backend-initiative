package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * InitiativeLegalDTO
 */
@Validated
@Data
//@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
//@Builder
public class InitiativeLegalDTO   {

  @NotBlank
  //@Pattern(regexp = "https://")
  @JsonProperty("privacyLink")
  private String privacyLink;

  @NotBlank
  @JsonProperty("tcLink")
  private String tcLink;

  @NotBlank
  @JsonProperty("regulationLink")
  private String regulationLink;

  @NotBlank
  @JsonProperty("dpiaLink")
  private String dpiaLink;

}
