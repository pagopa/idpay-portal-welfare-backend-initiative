package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
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
