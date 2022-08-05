package it.gov.pagopa.initiative.dto.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * StaticMccDTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfigMccDTO {
  @JsonProperty("code")
  private String code ;

  @JsonProperty("description")
  private String description;
}
