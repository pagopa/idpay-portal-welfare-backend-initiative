package it.gov.pagopa.initiative.dto.io.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * ServiceMetadata
 */

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class ServiceRequestMetadataDTO extends ServiceMetadataDTO{
  @JsonProperty("topic_id")
  @NotNull
  private Integer topicId;
}

