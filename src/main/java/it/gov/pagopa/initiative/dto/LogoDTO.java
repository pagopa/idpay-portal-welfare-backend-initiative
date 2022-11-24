package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * InitiativeAdditionalDTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class LogoDTO {
  @JsonProperty("logoFileName")
  private String logoFileName;

  @JsonProperty("logoURL")
  private String logoURL;

  @JsonProperty("logoUploadDate")
  private LocalDateTime logoUploadDate;
}
