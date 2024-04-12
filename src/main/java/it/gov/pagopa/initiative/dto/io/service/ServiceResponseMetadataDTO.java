package it.gov.pagopa.initiative.dto.io.service;


import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * ServiceMetadata
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class ServiceResponseMetadataDTO extends ServiceMetadataDTO{
  private TopicDTO topic;
}

