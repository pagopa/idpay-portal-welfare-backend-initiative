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
  
  @JsonProperty("privacyLink")
  private String privacyLink;

  @JsonProperty("tcLink")
  private String tcLink;

  @JsonProperty("regulationLink")
  private String regulationLink;

  @JsonProperty("dpiaLink")
  private String dpiaLink;

}
