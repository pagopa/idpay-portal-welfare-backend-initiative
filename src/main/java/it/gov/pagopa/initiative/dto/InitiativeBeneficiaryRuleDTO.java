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
@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class InitiativeBeneficiaryRuleDTO   {

  @JsonProperty("selfDeclarationCriteria")
  @Valid
  @Schema(
          description = "List of possible self criteria",
          anyOf = {SelfCriteriaBoolDTO.class, SelfCriteriaMultiDTO.class})
  private List<AnyOfInitiativeBeneficiaryRuleDTOSelfDeclarationCriteriaItems> selfDeclarationCriteria;

  @JsonProperty("automatedCriteria")
  @Valid
  private List<AutomatedCriteriaDTO> automatedCriteria;

}
