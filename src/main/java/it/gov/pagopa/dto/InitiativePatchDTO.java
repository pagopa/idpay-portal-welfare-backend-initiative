package it.gov.pagopa.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import it.gov.pagopa.dto.InitiativeAdditionalDTO;
import it.gov.pagopa.dto.InitiativeGeneralDTO;
import lombok.*;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

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
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-07-11T11:28:33.400Z[GMT]")


public class InitiativePatchDTO   {
  @JsonProperty("general")
  private InitiativeGeneralDTO general;

  @JsonProperty("additionalInfo")
  private InitiativeAdditionalDTO additionalInfo;

}
