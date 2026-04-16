package it.gov.pagopa.initiative.model;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.util.List;

/**
 * InitiativeAdditionalDTO
 */
@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class InitiativeAdditional {


  public enum ServiceScope{
    LOCAL,
    NATIONAL
  }
  private Boolean serviceIO;
  private String serviceId;
  private String serviceName;
  private ServiceScope serviceScope;
  private String description;
  private String privacyLink;
  private String tcLink;
  private List<Channel> channels;
  private String logoFileName;
  private Instant logoUploadDate;
  private String logoURL;
  private String thumbnailUrl;

}
