package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * InitiativeSummaryDTO
 */
@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class InitiativeSummaryDTO   {
  
  @JsonProperty("initiativeId")
  private String initiativeId;

  @JsonProperty("initiativeName")
  private String initiativeName;

  @JsonProperty("status")
  private String status;

  @JsonProperty("creationDate")
  private LocalDateTime creationDate;

  @JsonProperty("updateDate")
  private LocalDateTime updateDate;
}
