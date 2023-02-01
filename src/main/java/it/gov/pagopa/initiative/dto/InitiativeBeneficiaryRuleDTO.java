package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import it.gov.pagopa.initiative.utils.constraint.initiative.beneficiary.PDNDapiKeyMustExistForAtLeastOneAutoCriteriaConstraint;
import it.gov.pagopa.initiative.utils.validator.ValidationApiEnabledGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import java.util.List;

/**
 * InitiativeBeneficiaryRuleDTO
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
//@PDNDapiKeyMustExistForAtLeastOneAutoCriteriaConstraint(groups = ValidationApiEnabledGroup.class)
@SuperBuilder
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

  /**
   * PDND Key/Token Id
   */
  private String apiKeyClientId;

  /**
   * PDND Key/Token Assertion
   */
  private String apiKeyClientAssertion;

}
