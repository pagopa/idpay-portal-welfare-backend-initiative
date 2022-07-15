package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

/**
 * InitiativeBeneficiaryRuleDTO
 */
@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class InitiativeBeneficiaryRuleDTO   {


  // InitiativeBeneficiaryRuleDTO non deve essere null ma uno tra selfDeclarationCriteria e automatedCriteria dovrebbe esserlo??

  //! Come comportarsi con la scelta di criteri automated o self?

  //@NotEmpty
  @JsonProperty("selfDeclarationCriteria")
  @Valid
  private List<AnyOfInitiativeBeneficiaryRuleDTOSelfDeclarationCriteriaItems> selfDeclarationCriteria;

  //@NotEmpty
  @JsonProperty("automatedCriteria")
  @Valid
  private List<AutomatedCriteriaDTO> automatedCriteria;

}
