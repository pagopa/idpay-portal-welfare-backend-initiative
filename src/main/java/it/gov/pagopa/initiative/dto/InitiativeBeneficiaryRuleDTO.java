package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.List;

/**
 * InitiativeBeneficiaryRuleDTO
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
//@Builder
public class InitiativeBeneficiaryRuleDTO extends InitiativeOrganizationInfoDTO {

  @JsonProperty("selfDeclarationCriteria")
  @Schema(
          description = "List of possible self criteria",
          anyOf = {SelfCriteriaBoolDTO.class, SelfCriteriaMultiDTO.class})
  @Valid
  private List<AnyOfInitiativeBeneficiaryRuleDTOSelfDeclarationCriteriaItems> selfDeclarationCriteria;

  @JsonProperty("automatedCriteria")
  @Valid
  private List<AutomatedCriteriaDTO> automatedCriteria;

}
