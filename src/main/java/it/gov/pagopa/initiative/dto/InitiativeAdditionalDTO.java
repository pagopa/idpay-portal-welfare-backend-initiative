package it.gov.pagopa.initiative.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * InitiativeAdditionalDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-07-11T11:28:33.400Z[GMT]")

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class InitiativeAdditionalDTO   {

  @Size(min = 2, message = "At least 2 characters")
  @JsonProperty("serviceId")
  private String serviceId;

  @Size(min = 2, message = "At least 2 characters")
  @JsonProperty("serviceName")
  private String serviceName;

  @Size(min = 2, message = "At least 2 characters")
  @JsonProperty("argument")
  private String argument;

  @Size(min = 2, message = "At least 2 characters")
  @JsonProperty("description")
  private String description;

}
