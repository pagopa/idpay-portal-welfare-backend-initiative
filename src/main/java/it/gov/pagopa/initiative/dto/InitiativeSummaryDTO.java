package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.Instant;

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

  @JsonProperty("initiativeRewardType")
  private String initiativeRewardType;

  @JsonProperty("status")
  private String status;

  @JsonProperty("creationDate")
  private Instant creationDate;

  @JsonProperty("updateDate")
  private Instant updateDate;

  @JsonProperty("rankingEnabled")
  private Boolean rankingEnabled;

}
