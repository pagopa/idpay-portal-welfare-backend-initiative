package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

/**
 * InitiativeSummaryDTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InitiativeSummaryDTO   {
  
  @JsonProperty("initiativeId")
  private String initiativeId;

  @JsonProperty("initiativeName")
  private String initiativeName;

  @JsonProperty("status")
  private String status;

  @JsonProperty("deleted")
  private Boolean deleted;

  @JsonProperty("creationDate")
  private LocalDateTime creationDate;

  @JsonProperty("updateDate")
  private LocalDateTime updateDate;
}
