package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
@ToString
@Builder
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-07-11T11:28:33.400Z[GMT]")

public class InitiativeInfoDTO {

  @JsonProperty("general")
  @Valid
  //TODO aggiungere validatore custom che prosegue solo per status DRAFT
  private InitiativeGeneralDTO general;


  @Valid
  //TODO aggiungere validatore custom che prosegue solo per status DRAFT
  @JsonProperty("additionalInfo")
  private InitiativeAdditionalDTO additionalInfo;

}
