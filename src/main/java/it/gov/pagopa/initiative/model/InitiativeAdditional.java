package it.gov.pagopa.initiative.model;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
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

  // --- Nuove informazioni bonus per la card AppIO (predisposte da analisi Figma) ---
  /** Sezione "Chi può richiederlo" localizzata per lingua ("it", "en"). */
  private Map<String, String> eligibilityInfoMap;
  /** Sezione "Cosa offre" localizzata per lingua ("it", "en"). */
  private Map<String, String> benefitInfoMap;
  /** Sezione "Come richiederlo" localizzata per lingua ("it", "en"). */
  private Map<String, String> howToRequestInfoMap;
  /** Sezione "Come si usa" localizzata per lingua ("it", "en"). */
  private Map<String, String> howToUseInfoMap;
  /** Sezione "Ricorda" localizzata per lingua ("it", "en"). */
  private Map<String, String> reminderInfoMap;
  /** URL "Mostra prodotti compatibili". */
  private String compatibleProductsUrl;
  /** URL "Mostra l'elenco dei negozi". */
  private String storeListUrl;
  /** URL assistenza / CAC. */
  private String supportUrl;
  /** Data di apertura delle richieste. */
  private LocalDate requestStartDate;
  /** Giorni di validità del bonus. */
  private Integer bonusValidityDays;
  /** Data/ora di disponibilità del servizio. */
  private LocalDateTime serviceAvailabilityDate;
  /** Codice fiscale dell'ente erogatore. */
  private String organizationFiscalCode;

}
