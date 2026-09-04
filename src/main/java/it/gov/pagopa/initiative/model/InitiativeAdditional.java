package it.gov.pagopa.initiative.model;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
  private LocalDateTime logoUploadDate;
  private String logoURL;
  private String thumbnailUrl;
  /**
   * Testi configurabili della label CTA mostrata nella card IO, indicizzati per lingua ("it", "en").
   * Se assente/vuoto per una lingua si applica il fallback ai default in InitiativeConstants.CtaConstant.
   */
  private Map<String, String> ctaLabelMap;

}
