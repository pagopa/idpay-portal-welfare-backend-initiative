package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

/**
 * InitiativeBeneficiaryRuleDTO
 */
//@Validated
@Data
@NoArgsConstructor
@EqualsAndHashCode
//@Builder
public class InitiativeBeneficiaryRuleDTO   {


  // InitiativeBeneficiaryRuleDTO non deve essere null ma uno tra selfDeclarationCriteria e automatedCriteria dovrebbe esserlo??

  //! Come comportarsi con la scelta di criteri automated o self?

  //@NotEmpty
  @JsonProperty("selfDeclarationCriteria")
  @Valid
  @Schema(
          description = "List of possible self criteria",
          anyOf = {SelfCriteriaBoolDTO.class, SelfCriteriaMultiDTO.class})
  private List<AnyOfInitiativeBeneficiaryRuleDTOSelfDeclarationCriteriaItems> selfDeclarationCriteria;

  //@NotEmpty
  @JsonProperty("automatedCriteria")
  @Valid
  private List<AutomatedCriteriaDTO> automatedCriteria;

}
