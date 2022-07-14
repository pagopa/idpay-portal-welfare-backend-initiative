package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.validation.annotation.Validated;

/**
 * InitiativeLegalDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-07-10T13:24:21.794Z[GMT]")

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class InitiativeLegalDTO   {

  //@NotNull
  //@Pattern(regexp = "https://")
  @JsonProperty("privacyLink")
  private String privacyLink;

  //@NotEmpty
  //@Size(min = 8, message = "At least 8 characters") //'https://..' min 8 characters
  @JsonProperty("tcLink")
  private String tcLink;

  //@NotBlank
  //@Size(min = 8, message = "At least 8 characters") //'https://..' min 8 characters
  @JsonProperty("regulationLink")
  private String regulationLink;

  //@Size(min = 8, message = "At least 8 characters") //'https://..' min 8 characters
  @JsonProperty("dpiaLink")
  private String dpiaLink;

}
